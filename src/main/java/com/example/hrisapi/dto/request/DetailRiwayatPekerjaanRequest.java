package com.example.hrisapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class DetailRiwayatPekerjaanRequest {

    private String detailRiwayatPekerjaanId;

    private String namaPerusahaan;

    private String tahunMulai;

    private String tahunBerakhir;

    private String keterangan;
}
