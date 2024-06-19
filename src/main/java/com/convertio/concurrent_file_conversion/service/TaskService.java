package com.convertio.concurrent_file_conversion.service;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;

import com.convertio.concurrent_file_conversion.model.Task;
import com.convertio.concurrent_file_conversion.model.TaskStatus;
import com.convertio.concurrent_file_conversion.model.TaskType;
import com.convertio.concurrent_file_conversion.repository.TaskRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskService {

	private final TaskRepository repository;
	private final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
	private final ExecutorService executorService = Executors.newFixedThreadPool(10);
	
	@PostConstruct
	public void init() {
		// Start worker threads
		for (int i = 0; i < 10; i++) {
			executorService.submit(this::processTasks);
		}
	}
	
	public Optional<Task> submitTask(Long fileId, TaskType type) {
		Task newTask = Task.builder()
						.fileId(fileId)
						.status(TaskStatus.PENDING)
						.type(type)
						.build();
		
        if (repository.addTask(newTask)) {
        	taskQueue.add(newTask);
            return Optional.of(newTask);
        } else {
            return Optional.empty();
        }
	}
	
	private void processTasks() {
		while (true) {
			try {
				Task task = taskQueue.take();
				
				
				task.setStatus(TaskStatus.COMPLETED);
				repository.updateTask(task.getTaskId(), task);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (Exception e) {
				
			}
			
		}
	}
}
