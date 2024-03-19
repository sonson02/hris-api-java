package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportSPResponse {

    private String karyawanNip;

    private String karyawanName;

    private String jabatanName;

    private String namaProyek;

    private Integer suratPeringatan;

    private String tanggalSuratPeringatan;
}
