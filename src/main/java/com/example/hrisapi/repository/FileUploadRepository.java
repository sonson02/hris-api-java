package com.example.hrisapi.repository;

import com.example.hrisapi.entity.FileUploadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUploadEntity, UUID> {
    FileUploadEntity findByFileUploadId(UUID fileUploadId);
}
