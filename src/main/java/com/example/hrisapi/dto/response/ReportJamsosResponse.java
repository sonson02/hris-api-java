package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportJamsosResponse {

    private String karyawanNip;

    private String karyawanName;

    private String jabatanName;

    private String namaProyek;

    private Long gaji;

    private Double gajiTambahUangMakan;

    private Double bpjsKkJkk;

    private Double bpjsKkJkm;

    private Double bpjsKkJht;

    private Double bpjsKkJkkJkmJht;

    private Double bpjsKkBebanPegawai;

    private Double bpjsKsBebanPerusahaan;

    private Double bpjsKsBebanPegawai;

    private Double bpjsTkBebanPerusahaan;

    private Double bpjsTkBebanPegawai;

    private Double pphPasal21;

    private Double gajiDiterima;
}
