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
public class DetailPendidikanResponse {

    private String detailPendidikanId;

    private String pendidikan;

    private String namaSekolah;

    private String jurusan;

    private String asalSekolah;

    private String tahunMulai;

    private String tahunBerakhir;
}
