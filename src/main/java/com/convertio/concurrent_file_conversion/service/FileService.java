package com.convertio.concurrent_file_conversion.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.convertio.concurrent_file_conversion.model.File;
import com.convertio.concurrent_file_conversion.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileService {

	private final FileRepository repository;

	public File saveFile(String fileName, String contentType, byte[] fileData) throws IOException {
		try {
			File newFile = File.builder()
					.fileName(fileName)
					.contentType(contentType)
					.fileData(fileData)
					.build();
			return repository.save(newFile);
		} catch (RuntimeException e) {
			throw new IOException("Error saving file to repository", e);
		}

	}
}
