package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportKompensasiResponse {

    private String karyawanNip;

    private String karyawanName;

    private String jabatanName;

    private String tanggalBerlakuKompensasi;

    private Double kompensasiDiterima;

    private Double managementFee;

    private Double total;
}
