package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KontrakKerjaResponse {
    private UUID kontrakId;

    private String karyawanNip;

    private String karyawanName;

    private String namaProyek;

    private String unitName;

    private String jabatanName;

    private String periodKontrak;

    private Date tglMasukKerja;

    private Date tglHabisKontrak;

    private Boolean isActive;

    private String nonik;

    private Double gaji;

    private Double uangTelekomunikasi;

    private String kontrakKode;
}
