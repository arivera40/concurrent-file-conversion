package com.convertio.concurrent_file_conversion.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.convertio.concurrent_file_conversion.model.File;

import com.convertio.concurrent_file_conversion.service.FileService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class FileController {
	
	private final FileService fileService;

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
		}

		try {
			File savedFile = fileService.saveFile(
								file.getOriginalFilename(),
								file.getContentType(),
								file.getBytes());
			
			return ResponseEntity.ok(savedFile.getFileId());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error uploading file: " + e.getMessage());
		}
	}
}
