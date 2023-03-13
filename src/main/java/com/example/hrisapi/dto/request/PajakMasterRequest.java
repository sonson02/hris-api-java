package com.example.hrisapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class PajakMasterRequest {

    private UUID pajakId;

    private Boolean isActive;

    private Double pajakPersen;

    private String pajakStatus;

    private String pajakType;

    private String usrUpdate;
}
