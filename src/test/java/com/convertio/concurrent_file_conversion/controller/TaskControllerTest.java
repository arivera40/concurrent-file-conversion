package com.convertio.concurrent_file_conversion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.convertio.concurrent_file_conversion.model.Task;
import com.convertio.concurrent_file_conversion.model.TaskStatus;
import com.convertio.concurrent_file_conversion.model.TaskType;
import com.convertio.concurrent_file_conversion.payload.SubmitTaskRequest;
import com.convertio.concurrent_file_conversion.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private TaskService taskService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private SubmitTaskRequest submitTaskRequest;
	
	@BeforeEach
	public void setUp() {
		submitTaskRequest = new SubmitTaskRequest();
		submitTaskRequest.setFileId(1L);
		submitTaskRequest.setTaskType(TaskType.CSV_TO_JSON);
	}
	
	@Test
	public void testSubmitTaskSuccess() throws Exception {
		// Arrange
		Task mockTask = Task.builder()
						.fileId(1L)
						.type(TaskType.CSV_TO_JSON)
						.status(TaskStatus.PENDING)
						.build();
		
		when(taskService.submitTask(anyLong(), any(TaskType.class)))
			.thenReturn(Optional.of(mockTask));
		
		// Act & Assert
		mockMvc.perform(post("/tasks/submit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(submitTaskRequest)))
		.andExpect(status().isOk())
		.andExpect(content().json(objectMapper.writeValueAsString(mockTask)));
	}
	
	@Test
	public void testSubmitTaskAlreadyExists() throws Exception {
		// Arrange
		when(taskService.submitTask(anyLong(), any(TaskType.class)))
			.thenReturn(Optional.empty());
		
		// Act & Assert
		mockMvc.perform(post("/tasks/submit")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(submitTaskRequest)))
		.andExpect(status().isBadRequest())
		.andExpect(content().string("Task already exists."));
	}
}
