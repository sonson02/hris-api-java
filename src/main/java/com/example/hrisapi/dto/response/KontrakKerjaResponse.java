package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KontrakKerjaResponse {
    private UUID kontrakId;

    private String karyawanNip;

    private String karyawanName;

    private UUID tempatTugasId;

    private UUID unitId;

    private UUID jabatanId;

    private String periodKontrak;

    private String tglMasukKerja;

    private String tglHabisKontrak;

    private Boolean isActive;

    private String nonik;

    private Double gaji;

    private Double uangTelekomunikasi;

    private String kontrakKode;

    private String tipeTunjangan;

    private String tempatTinggal;

    private String tanggalLahir;

    private String requestNo;

    private String requestDate;

    private Double uangMakan;
}
