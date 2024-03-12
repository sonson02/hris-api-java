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

    private String nonik;

    private String tempatLahir;

    private String tanggalLahir;

    private String kontrakKode;

    private String requestNo;

    private Long gaji;

    private String tglMasukKerja;

    private String tglHabisKontrak;

    private UUID tempatTugasId;

    private String namaProyek;

    private UUID unitId;

    private String unitName;

    private UUID jabatanId;

    private String jabatanName;

    private Boolean isActive;

    private String periodKontrak;

    private String usrUpdate;

    private String requestDate;

    private Long tunjanganKomunikasi;

    private Long uangMakan;

    private Long tunjangan;

    private Long tunjanganKhusus;

    private Long tunjanganVariable;

    private String uploadDocKontrak;

    private Boolean isUpload;
}
