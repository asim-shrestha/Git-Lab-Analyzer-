package com.eris.gitlabanalyzer.repository;

import com.eris.gitlabanalyzer.model.AnalysisRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRunRepository extends JpaRepository<AnalysisRun, Long> {
    List<AnalysisRun> findAnalysisRunByOwnerUserId(Long userId);
}
