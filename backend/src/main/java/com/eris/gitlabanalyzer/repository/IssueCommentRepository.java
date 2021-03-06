package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IssueCommentRepository extends JpaRepository<Note, Long> {
    @Query("select n from Note n where n.projectId = ?1 and n.noteableType='Issue' order by n.createdAt desc")
    List<Note> findAllByProjectId(Long projectId);

    @Query("select n from Note n where n.projectId = ?1 and n.createdAt >= ?2 and n.createdAt <= ?3 and n.noteableType='Issue' order by n.createdAt desc")
    List<Note> findAllByProjectIdAndDateRange(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime);

    @Query("select n from Note n where n.projectId = ?1 " +
            "and n.gitManagementUser.id = ?2 " +
            "and n.createdAt >= ?3 and n.createdAt <= ?4 " +
            "and n.noteableType='Issue' order by n.createdAt desc")
    List<Note> findAllByProjectIdAndGitManagementUserIdAndDateRange(
            Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime
    );

    @Query("select n from Note n where n.gitLabNoteId = ?1 and n.projectId = ?2 and n.noteableType='Issue'")
    Optional<Note> findByGitLabNoteIdAndProjectId(Long gitLabNoteId, Long projectId);
}
