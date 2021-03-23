package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorRequestBody;
import com.eris.gitlabanalyzer.viewmodel.CommitAuthorView;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommitService {
    MergeRequestRepository mergeRequestRepository;
    CommitRepository commitRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;
    CommitCommentRepository commitCommentRepository;
    ScoreService scoreService;
    CommitAuthorRepository commitAuthorRepository;
    AnalysisRunService analysisRunService;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public CommitService(MergeRequestRepository mergeRequestRepository, CommitRepository commitRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository, CommitCommentRepository commitCommentRepository, ScoreService scoreService, CommitAuthorRepository commitAuthorRepository, AnalysisRunService analysisRunService) {
        this.mergeRequestRepository = mergeRequestRepository;
        this.commitRepository = commitRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
        this.commitCommentRepository = commitCommentRepository;
        this.scoreService = scoreService;
        this.commitAuthorRepository = commitAuthorRepository;
        this.analysisRunService = analysisRunService;
    }

    public String splitEmail(String email) {
        String[] stringArr = email.split("@");
        return stringArr[0];
    }


    public void saveCommitInfo(AnalysisRun analysisRun, Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        //Save commits associated with each merge request
        List<MergeRequest> mergeRequestList = mergeRequestRepository.findAllByProjectId(project.getId());
        List<String> mrCommitShas = new ArrayList<>(); //Used to filter for the case of orphan commits

        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);

        Double progress;
        Double startOfProgressRange = AnalysisRunProgress.Progress.AtStartOfImportingCommits.getValue();
        Double endOfProgressRange = AnalysisRunProgress.Progress.AtStartOfImportingOrphanCommits.getValue();

        for(int i = 0; i < mergeRequestList.size();i++){
            progress = startOfProgressRange + (endOfProgressRange-startOfProgressRange) * (i+1)/mergeRequestList.size();
            analysisRunService.updateProgress(analysisRun, "Importing commits for "+ (i+1) +"/"+mergeRequestList.size() + " merge requests",progress);
            var gitLabCommits = gitLabService.getMergeRequestCommits(project.getGitLabProjectId(), mergeRequestList.get(i).getIid());
            saveCommitHelper(project, mergeRequestList.get(i), gitLabCommits, mrCommitShas);
        }

        //Save orphan commits
        var gitLabCommits = gitLabService.getCommits(project.getGitLabProjectId(), startDateTime, endDateTime)
                                                           .filter(gitLabCommit -> !mrCommitShas.contains(gitLabCommit.getSha()));
        analysisRunService.updateProgress(analysisRun, "Importing orphan commits", AnalysisRunProgress.Progress.AtStartOfImportingOrphanCommits.getValue());
        saveCommitHelper(project, null, gitLabCommits, mrCommitShas);
    }

    public void saveCommitHelper(Project project, MergeRequest mergeRequest,Flux<GitLabCommit> gitLabCommits, List<String> mrCommitShas){
        var gitLabCommitList = gitLabCommits.collectList().block();

        gitLabCommitList.forEach(gitLabCommit -> {
                    Commit commit = commitRepository.findByCommitShaAndProjectId(gitLabCommit.getSha(),project.getId());
                    if(commit != null){return;}

                    saveCommitAuthor(project,gitLabCommit);

                    commit = new Commit(
                            gitLabCommit.getSha(),
                            gitLabCommit.getTitle(),
                            gitLabCommit.getAuthorName(),
                            gitLabCommit.getAuthorEmail(),
                            gitLabCommit.getCreatedAt().withOffsetSameInstant(ZoneOffset.UTC),
                            gitLabCommit.getWebUrl(),
                            project
                    );

                    if(mergeRequest != null){
                        mergeRequest.addCommit(commit);
                        mrCommitShas.add(gitLabCommit.getSha());
                    }


                    commit = commitRepository.save(commit);
                    saveCommitComment(project, commit);
                    scoreService.saveCommitDiffMetrics(commit);
                }
        );

    }

    public CommitAuthor saveCommitAuthor(Project project, GitLabCommit gitLabCommit){
        CommitAuthor commitAuthor = commitAuthorRepository.findByAuthorNameAndAuthorEmailAndProjectId(
                gitLabCommit.getAuthorName(),
                gitLabCommit.getAuthorEmail(),
                project.getId()
        );

        if(commitAuthor != null){
            return commitAuthor;
        }

        commitAuthor = new CommitAuthor(gitLabCommit.getAuthorName(), gitLabCommit.getAuthorEmail(), project);

        //First attempt using author username extracted from email
        String username = splitEmail(gitLabCommit.getAuthorEmail());
        GitManagementUser gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(username, serverUrl);

        if(gitManagementUser == null){
            //Second attempt using author name
            gitManagementUser = gitManagementUserRepository.findByUsernameAndServerUrl(gitLabCommit.getAuthorName(),serverUrl);
        }

        if(gitManagementUser != null){
            commitAuthor.setGitManagementUser(gitManagementUser);
        }

        return commitAuthorRepository.save(commitAuthor);
    }

    public void saveCommitComment(Project project, Commit commit){
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
        var gitLabCommitComments = gitLabService.getCommitComments(project.getGitLabProjectId(), commit.getSha());
        var gitLabCommitCommentList = gitLabCommitComments.collectList().block();

        gitLabCommitCommentList.parallelStream().forEach(gitLabCommitComment -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabCommitComment.getAuthor().getId(),serverUrl);
            if(gitManagementUser == null){
                return;
            }
            CommitComment commitComment = commitCommentRepository.findByGitLabUserIdAndCreatedAtAndCommitSha(gitLabCommitComment.getAuthor().getId(),gitLabCommitComment.getCreatedAt(),commit.getSha());
            if(commitComment == null){
                commitComment = new CommitComment(
                        gitManagementUser,
                        commit,
                        gitLabCommitComment.getNote(),
                        gitLabCommitComment.getCreatedAt());
            }
            commitCommentRepository.save(commitComment);
        });
    }

    public List<CommitAuthorView> getCommitAuthors(Long projectId){
        List<CommitAuthorView> mappedCommitAuthors = commitAuthorRepository.findByProjectId(projectId);
        List<CommitAuthorView> unmappedCommitAuthors = commitAuthorRepository.findUnmappedCommitAuthorsByProjectId(projectId);
        return Stream.of(mappedCommitAuthors, unmappedCommitAuthors).flatMap(Collection::stream)
                                                                    .sorted(Comparator.comparing(CommitAuthorView::getAuthorName))
                                                                    .collect(Collectors.toList());
    }

    public List<CommitAuthorView> getUnmappedCommitAuthors(Long projectId){
        return commitAuthorRepository.findUnmappedCommitAuthorsByProjectId(projectId);
    }

    public void mapNewCommitAuthors(Long projectId, List<CommitAuthorRequestBody> commitAuthors){
        commitAuthors.forEach(commitAuthor -> {
            if(commitAuthor.getMappedGitManagementUserId() == null){
                return;
            }

            commitAuthorRepository.updateCommitAuthors(
                    commitAuthor.getMappedGitManagementUserId(),
                    commitAuthor.getAuthorName(),
                    commitAuthor.getAuthorEmail(),
                    projectId);
        });
    }

    public List<Commit> getCommits(Long projectId){
        return commitRepository.findAllByProjectId(projectId);
    }

    public List<Commit> getCommitsOfGitManagementUser(Long projectId, Long gitManagementUserId){
        return commitRepository.findByProjectIdAndGitManagementUserId(projectId, gitManagementUserId);
    }

}