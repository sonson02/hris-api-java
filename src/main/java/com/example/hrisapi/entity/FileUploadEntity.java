package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_upload", schema = "hrisnew")
public class FileUploadEntity {

    @Id
    private UUID fileUploadId;

    private String fileLocation;

    private String fileName;

    private String fileObject;

    private String fileObjectId;

    private String fileType;
}
