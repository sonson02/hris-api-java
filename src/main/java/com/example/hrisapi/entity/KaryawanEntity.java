package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "karyawan", schema = "dbo")
public class KaryawanEntity {

    @Id
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

    private String pendidikanTerakhir;

    private String statusNikah;

    private Date tanggalLahir;

    private String tempatTinggal;

    private UUID tempatTugasId;

    private Date tglHabisKontrak;

    private Date tglMasukKerja;

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
}
