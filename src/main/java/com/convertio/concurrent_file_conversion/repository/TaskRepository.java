package com.convertio.concurrent_file_conversion.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.convertio.concurrent_file_conversion.model.Task;

@Repository
public class TaskRepository {

	ConcurrentHashMap<String, Task> taskMap = new ConcurrentHashMap<>();
	
	// Get task by task id.
	public Task getTask(String id) {
		return taskMap.get(id);
	}
	
	// Add a new task.
	public boolean addTask(Task task) {
		if (!taskMap.containsKey(task.getTaskId())) {
			taskMap.put(task.getTaskId(), task);
			return true;
		}
		return false;
	}
	
	// Delete an existing task by id.
	public boolean deleteTask(String id) {
		if (taskMap.containsKey(id)) {
			taskMap.remove(id);
			return true;
		}
		return false;
	}
	
	// Delete an existing task by object.
	public boolean deleteTask(Task task) {
		if (taskMap.containsKey(task.getTaskId())) {
			taskMap.remove(task.getTaskId());
			return true;
		}
		return false;
	}
	
	// Update an existing task by id.
	public boolean updateTask(String id, Task task) {
		if (taskMap.containsKey(id)) {
			taskMap.put(id, task);
			return true;
		}
		return false;
	}
	
	// Update an existing task by object.
	public boolean updateTask(Task oldTask, Task newTask) {
		if (taskMap.containsKey(oldTask.getTaskId())) {
			taskMap.remove(oldTask.getTaskId());
			taskMap.put(newTask.getTaskId(), newTask);
			return true;
		}
		return false;
	}
}
