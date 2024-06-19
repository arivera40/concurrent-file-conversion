package com.convertio.concurrent_file_conversion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.convertio.concurrent_file_conversion.model.File;

public interface FileRepository extends JpaRepository<File, Long> {
	Optional<File> findByFileId(Long fileId);
}
