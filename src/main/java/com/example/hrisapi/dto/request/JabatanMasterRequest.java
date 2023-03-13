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
public class JabatanMasterRequest {
    private UUID jabatanId;

    private String jabatanDesc;

    private String jabatanName;

    private Double tunjangan;

    private Boolean isActive;

}
