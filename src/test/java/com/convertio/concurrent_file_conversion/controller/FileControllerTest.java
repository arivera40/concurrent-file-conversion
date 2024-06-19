package com.convertio.concurrent_file_conversion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.convertio.concurrent_file_conversion.model.File;
import com.convertio.concurrent_file_conversion.service.FileService;

@WebMvcTest(FileController.class)
public class FileControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FileService fileService;

	private MockMultipartFile mockFile;

	@BeforeEach
	public void setUp() {
		mockFile = new MockMultipartFile("file", "test.csv", MediaType.TEXT_PLAIN_VALUE,
				"col1,col2\nval1,val2".getBytes());
	}

	@Test
	public void testUploadFileSuccess() throws Exception {
		// Setup
		File mockSavedFile = new File();
		mockSavedFile.setFileId(1L);
		when(fileService.saveFile(anyString(), anyString(), any(byte[].class))).thenReturn(mockSavedFile);

		// Act & Assert
		mockMvc.perform(multipart("/files/upload")
				.file(mockFile))
				.andExpect(status().isOk())
				.andExpect(content().string("1"));
	}

	@Test
	public void testUploadFileEmpty() throws Exception {
		// Act & Assert
		mockMvc.perform(multipart("/files/upload")
				.file(new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, new byte[0])))
				.andExpect(status().isBadRequest());
	}

	@Test
    public void testUploadFileIOException() throws Exception {
        // Arrange
        when(fileService.saveFile(anyString(), anyString(), any(byte[].class)))
                .thenThrow(new IOException("Test IOException"));

        // Act & Assert
        mockMvc.perform(multipart("/files/upload")
                .file(mockFile))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error uploading file: Test IOException"));
    }
}
