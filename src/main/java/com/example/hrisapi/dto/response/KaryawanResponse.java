package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KaryawanResponse {
    private UUID karyawanId;

    private String agama;

    private String alamatRumah;

    private UUID bankId;

    private Date dtmUpdate;

    private String email;

    private Long gaji;

    private String gender;

    private Boolean isActive;

    private String karyawanName;

    private String karyawanNip;

    private String noBpjsKesehatan;

    private String noBpjsTenagaKerja;

    private String noHandphone;

    private String nokk;

    private String nonik;

    private String nonpwp;

    private String noRekening;

    private List<DetailPendidikanResponse> pendidikanTerakhir;

    private String statusNikah;

    private String tanggalLahir;

    private String tempatTinggal;

    private UUID tempatTugasId;

    private String tglHabisKontrak;

    private String tglMasukKerja;

    private String tipeTunjangan;

    private Double uangTelekomunikasi;

    private UUID unitId;

    private String usrUpdate;

    private UUID jabatanId;

    private UUID fileUploadId;

    private Double uangMakan;

    private String golonganDarah;

    private String namaAyahKandung;

    private String namaIbuKandung;

    private String keluargaYangDihubungi;

    private String namaKeluargaYangDihubungi;

    private String alamatDomisili;

    private String noHpKeluarga;

    private String rekeningAtasNama;

    private List<DetailRiwayatPekerjaanResponse> riwayatPekerjaan;

    private Double tunjanganJabatan;
}
