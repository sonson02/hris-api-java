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
public class KaryawanByNipResponse {
    private String karyawanNip;

    private String karyawanName;

    private String nonik;

    private String tempatTinggal;

    private String tanggalLahir;

    private String agama;

    private String alamatRumah;

    private String asalSekolah;

    private UUID bankId;

    private Date dtmUpdate;

    private String email;

    private Double gaji;

    private String gender;

    private Boolean isActive;

    private String jurusan;

    private String noBpjsKesehatan;

    private String noBpjsTenagaKerja;

    private String noHandphone;

    private String nokk;

    private String nonpwp;

    private String noRekening;

    private String pendidikanTerakhir;

    private String statusNikah;

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

    private String riwayatPekerjaan;

    private Double tunjanganJabatan;
}
