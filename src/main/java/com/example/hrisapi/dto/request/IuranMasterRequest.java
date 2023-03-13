package com.example.hrisapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class IuranMasterRequest {

    private UUID iuranId;

    private Boolean isActive;

    private Double iuranBeban;

    private Double iuranPersen;

    private String iuranType;

    private String usrUpdate;
}
