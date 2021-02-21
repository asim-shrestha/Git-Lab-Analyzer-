package com.eris.gitlabanalyzer.model.types.task;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class TaskCompletionStatus {
    private int count;
    @JsonProperty("completed_count")
    private int completedCount;
}