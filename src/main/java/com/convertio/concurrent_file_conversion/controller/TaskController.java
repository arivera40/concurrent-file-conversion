package com.convertio.concurrent_file_conversion.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.convertio.concurrent_file_conversion.model.Task;
import com.convertio.concurrent_file_conversion.model.TaskType;
import com.convertio.concurrent_file_conversion.payload.SubmitTaskRequest;
import com.convertio.concurrent_file_conversion.service.TaskService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	private final TaskService taskService;
	
	@PostMapping("submit")
	public ResponseEntity<?> submitTask(@RequestBody SubmitTaskRequest taskRequest) {
		Optional<Task> optionalTask = taskService.submitTask(taskRequest.getFileId(), taskRequest.getTaskType());
		
		if (optionalTask.isPresent()) {
			return ResponseEntity.ok(optionalTask.get());
		}
		
		return ResponseEntity.badRequest().body("Task already exists.");
	}
}
