package com.convertio.concurrent_file_conversion.processor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.convertio.concurrent_file_conversion.model.Task;
import com.convertio.concurrent_file_conversion.model.TaskStatus;
import com.convertio.concurrent_file_conversion.model.TaskType;
import com.convertio.concurrent_file_conversion.model.File;
import com.convertio.concurrent_file_conversion.repository.FileRepository;
import com.convertio.concurrent_file_conversion.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskProcessor {

	private final TaskRepository taskRepository;

	private final FileRepository fileRepository;

	public void processTask(Task task) {
		task.setStatus(TaskStatus.IN_PROGRESS);
		Optional<File> optionalFile = fileRepository.findByFileId(task.getFileId());

		if (optionalFile.isEmpty()) {
			task.setStatus(TaskStatus.FAILED);
			return;
		}

		File file = optionalFile.get();

		// Check if File Content-Type matches assigned Task-Type

		try {
			if (task.getType() == TaskType.CSV_TO_JSON) {
				java.io.File csvFile = convertCsvToJson(file);
				task.setStatus(TaskStatus.COMPLETED);
			} else if (task.getType() == TaskType.JSON_TO_XML) {
				convertJsonToXML(file);
			} else {
				convertXmlToJson(file);
			}
		} catch (Exception e) {
			log.error("Failed to convert CSV to JSON", e);
            task.setStatus(TaskStatus.FAILED);
		}
		
	}

	private java.io.File convertCsvToJson(File file) throws IOException, CsvException {
		// Create a temporary file for the JSON output
        java.io.File jsonFile = java.io.File.createTempFile("converted_", ".json");
        byte[] fileData = file.getFileData();
        
        try (
    		Reader reader = new InputStreamReader(new ByteArrayInputStream(fileData), StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReader(reader);
		) {
        	List<String[]> csvData = csvReader.readAll();
        	
        	if (csvData.isEmpty()) {
        		throw new IOException("CSV data is empty");
        	}
        	
        	// Create JSON array node
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayNode jsonArray = objectMapper.createArrayNode();
            
            // Assume first row is the header
            String[] headers = csvData.get(0);
            
            for (int i = 1; i < csvData.size(); i++) {
            	String[] row = csvData.get(i);
            	ObjectNode jsonObject = objectMapper.createObjectNode();
            	
            	for (int j = 0; j < headers.length; j++) {
            		jsonObject.put(headers[j], row[j]);
            	}
            	
            	jsonArray.add(jsonObject);
            }
            
            // Write JSON array to file
            objectMapper.writeValue(jsonFile, jsonArray);
        }
        
        return jsonFile;
	}

	private void convertXmlToJson(File file) {
		// TODO Auto-generated method stub

	}

	private void convertJsonToXML(File file) {
		// TODO Auto-generated method stub

	}
}
