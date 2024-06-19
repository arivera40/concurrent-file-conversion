package com.convertio.concurrent_file_conversion.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	@Setter
	private String taskId;
	private Long fileId;
	private TaskType type;
	private TaskStatus status;
	
    @Builder
    public Task(Long fileId, TaskType type, TaskStatus status) {
        this.taskId = UUID.randomUUID().toString();
        this.fileId = fileId;
        this.type = type;
        this.status = status;
    }
}
