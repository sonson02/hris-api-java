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
@Table(name = "template", schema = "hrisnew")
public class TemplateEntity {

    @Id
    private UUID templateId;

    private Boolean isActive;

    private String templateHtml;

    private String templateName;
}
