package com.example.hrisapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "karyawan_history", schema = "dbo")
public class KaryawanHistoryEntity {

    @Id
    private UUID karyawanHistoryId;

    private UUID karyawanId;

    private String agama;

    private String alamatRumah;

    private UUID bankId;

    private Date dtmUpdate;

    private String email;

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

    private String tempatLahir;

    private String usrUpdate;

    private UUID fileUploadId;

    private String golonganDarah;

    private String namaAyahKandung;

    private String namaIbuKandung;

    private String keluargaYangDihubungi;

    private String namaKeluargaYangDihubungi;

    private String alamatDomisili;

    private String noHpKeluarga;

    private String rekeningAtasNama;

    private String riwayatPekerjaan;

    private String photoKaryawan;

    private Integer suratPeringatan;

    private Date tanggalSuratPeringatan;

    private Date createdDate;

    private String flagAction;
}
