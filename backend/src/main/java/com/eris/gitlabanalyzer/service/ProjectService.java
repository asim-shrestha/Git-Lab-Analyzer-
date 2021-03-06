package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabCommit;
import com.eris.gitlabanalyzer.model.gitlabresponse.GitLabMergeRequest;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import com.eris.gitlabanalyzer.repository.ServerRepository;
import com.eris.gitlabanalyzer.repository.UserProjectPermissionRepository;
import com.eris.gitlabanalyzer.repository.UserServerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ServerRepository serverRepository;
    private final UserProjectPermissionRepository userProjectPermissionRepository;
    private final UserServerRepository userServerRepository;
    private final GitLabService requestScopeGitLabService;

    public ProjectService(ProjectRepository projectRepository, ServerRepository serverRepository, UserProjectPermissionRepository userProjectPermissionRepository, UserServerRepository userServerRepository, GitLabService requestScopeGitLabService) {
        this.projectRepository = projectRepository;
        this.serverRepository = serverRepository;
        this.userProjectPermissionRepository = userProjectPermissionRepository;
        this.userServerRepository = userServerRepository;
        this.requestScopeGitLabService = requestScopeGitLabService;
    }

    private void createUserProjectPermission(User user, Server server, Project project)
    {
        var queryPermission = userProjectPermissionRepository.findByUserIdAndServerIdAndProjectId(
                user.getId(),
                server.getId(),
                project.getId()
        );

        if(queryPermission.isEmpty()) {
            UserProjectPermission userProjectPermission = new UserProjectPermission(user, project, server);
            userProjectPermissionRepository.save(userProjectPermission);
        }
    }

    public Project saveProjectInfo(User user, Long serverId, Long gitLabProjectId) {
        var server = serverRepository.findById(serverId).get();
        var gitLabProject = requestScopeGitLabService.getProject(gitLabProjectId).block();
        Optional<Project> projectOptional = projectRepository.findByGitlabProjectIdAndServerId(gitLabProjectId, server.getId());
        if(projectOptional.isPresent()){
            createUserProjectPermission(user, server, projectOptional.get());
            return projectOptional.get();
        }

        Project project = projectRepository.save(new Project(
                gitLabProjectId,
                gitLabProject.getName(),
                gitLabProject.getNameWithNamespace(),
                gitLabProject.getWebUrl(),
                server
        ));
        createUserProjectPermission(user, server, project);
        return project;
    }

    public RawTimeLineProjectData getTimeLineProjectData(Long gitLabProjectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        var mergeRequests = requestScopeGitLabService.getMergeRequests(gitLabProjectId, startDateTime, endDateTime);

        // for all items in mergeRequests call get commits
        // for all items in commits call get diff
        // for all items in merge request get diff
        var rawMergeRequestData = mergeRequests
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((mergeRequest) -> getRawMergeRequestData(mergeRequest, gitLabProjectId))
                .sorted((mr1, mr2) -> (int) (mr1.getGitLabMergeRequest().getIid() - mr2.getGitLabMergeRequest().getIid()));


        // for all commits NOT in merge commits get diff
        var mergeRequestCommitIds = getMergeRequestCommitIds(rawMergeRequestData);
        var commits = requestScopeGitLabService.getCommits(gitLabProjectId, startDateTime, endDateTime);
        var orphanCommits = getOrphanCommits(commits, mergeRequestCommitIds);
        var rawOrphanCommitData = orphanCommits
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((commit) -> getRawCommitData(commit, gitLabProjectId))
                .sorted(Comparator.comparing(c -> c.getGitLabCommit().getCreatedAt()));

        var rawProjectData = new RawTimeLineProjectData(gitLabProjectId, startDateTime, endDateTime, rawMergeRequestData, rawOrphanCommitData);

        return rawProjectData;
    }


    private RawMergeRequestData getRawMergeRequestData(GitLabMergeRequest mergeRequest, Long gitLabProjectId) {
        var gitLabCommits = requestScopeGitLabService.getMergeRequestCommits(gitLabProjectId, mergeRequest.getIid());
        var rawCommitData = gitLabCommits
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map((commit) -> getRawCommitData(commit, gitLabProjectId))
                .sorted(Comparator.comparing(c -> c.getGitLabCommit().getCreatedAt()));

        var gitLabDiff = requestScopeGitLabService.getMergeRequestDiff(gitLabProjectId, mergeRequest.getIid());

        return new RawMergeRequestData(rawCommitData, gitLabDiff, mergeRequest);
    }

    private RawCommitData getRawCommitData(GitLabCommit commit, Long gitLabProjectId) {
        var changes = requestScopeGitLabService.getCommitDiff(gitLabProjectId, commit.getSha());
        return new RawCommitData(commit, changes);
    }

    private Mono<Set<String>> getMergeRequestCommitIds(Flux<RawMergeRequestData> mergeRequestData) {
        return mergeRequestData.flatMap(mergeRequest -> mergeRequest.getFluxRawCommitData())
                .map(commit -> commit.getFluxGitLabCommit().getSha())
                .collect(Collectors.toSet());
    }

    private Flux<GitLabCommit> getOrphanCommits(Flux<GitLabCommit> commits, Mono<Set<String>> mrCommitIds) {
        return mrCommitIds.flatMapMany(commitIds -> commits.filter(gitLabCommit -> !commitIds.contains(gitLabCommit.getSha())));
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findProjectById(projectId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id" + projectId)
        );
    }
}
