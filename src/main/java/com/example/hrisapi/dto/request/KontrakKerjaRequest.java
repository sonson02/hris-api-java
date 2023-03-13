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
public class KontrakKerjaRequest {

    private UUID kontrakId;

    private String karyawanNip;

    private String nonik;

    private String unitName;

    private String namaProyek;

    private String jabatanName;

    private Double gaji;

    private Double uangTelekomunikasi;

    private String karyawanName;

    private String tempatTinggal;

    private Date tanggalLahir;

    private String tipeTunjangan;

    private Date tglMasukKerja;

    private Date tglHabisKontrak;
}
