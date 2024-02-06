package com.example.hrisapi.dto.response;

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
public class StopKaryawanResponse {

    private UUID karyawanId;
}
