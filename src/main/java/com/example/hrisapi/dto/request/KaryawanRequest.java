package com.example.hrisapi.dto.request;

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
public class KaryawanRequest {
    private UUID karyawanId;

    private String karyawanNip;

    private String karyawanName;

    private String tempatTinggal;

    private Date tanggalLahir;

    private String agama;

    private String gender;

    private String statusNikah;

    private String alamatRumah;

    private UUID bankId;

    private String noRekening;

    private String email;

    private String noHandphone;

    private String nonik;

    private String nokk;

    private String nonpwp;

    private String pendidikanTerakhir;

    private String jurusan;

    private String asalSekolah;

    private String noBpjsTenagaKerja;

    private String noBpjsKesehatan;

    private String lampiranCv;
}
