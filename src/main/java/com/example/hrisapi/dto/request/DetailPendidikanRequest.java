package com.example.hrisapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.validation.annotation.Validated;

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
public class DetailPendidikanRequest {

    private String detailPendidikanId;

    private String namaSekolah;

    private String jurusan;

    private String asalSekolah;

    private String tahunMulai;

    private String tahunBerakhir;
}
