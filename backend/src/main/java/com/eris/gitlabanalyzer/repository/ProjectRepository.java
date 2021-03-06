package com.eris.gitlabanalyzer.repository;
import com.eris.gitlabanalyzer.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select p from Project p where p.gitLabProjectId = ?1 and p.server.id = ?2")
    Optional<Project> findByGitlabProjectIdAndServerId(Long gitLabProjectId, Long serverId);

    Optional<Project> findProjectById(Long id);
}
