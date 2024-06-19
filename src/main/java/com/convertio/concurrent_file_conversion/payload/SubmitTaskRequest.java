package com.convertio.concurrent_file_conversion.payload;

import com.convertio.concurrent_file_conversion.model.TaskType;

import lombok.Data;

@Data
public class SubmitTaskRequest {
    private Long fileId;
    private TaskType taskType;
}
