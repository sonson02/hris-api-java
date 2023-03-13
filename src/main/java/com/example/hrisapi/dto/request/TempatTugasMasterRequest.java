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
public class TempatTugasMasterRequest {

    private UUID tempatTugasId;

    private Boolean isActive;

    private UUID jabatanId;

    private String lokasiTempatTugas;

    private String namaProyek;

    private Double nominalTunjangan;

    private Double tunjanganTetap;

    private UUID unitId;

    private String usrUpdate;
}
