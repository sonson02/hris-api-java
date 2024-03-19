package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListKaryawanResponse {
    private UUID karyawanId;

    private String agama;

    private String alamatRumah;

    private UUID bankId;

    private String bankName;

    private Date dtmUpdate;

    private String email;

    private String gender;

    private Boolean isActive;

    private String karyawanNip;

    private String karyawanName;

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

    private List<DetailRiwayatPekerjaanResponse> riwayatPekerjaan;

    private String namaProyek;

    private String unitName;

    private String jabatanName;

    private String tglHabisKontrak;

    private Integer periodKontrak;

    private String noKontrak;

    private UUID kontrakId;

    private Integer suratPeringatan;

    private String tanggalSuratPeringatan;

}
