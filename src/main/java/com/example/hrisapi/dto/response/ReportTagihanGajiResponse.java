package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportTagihanGajiResponse {

    private String karyawanNip;

    private String karyawanName;

    private String jabatanName;

    private String namaProyek;

    private Long gaji;

    private Double tunjangan;

    private Double gajiDibayar;

    private Double manajemenFee;

    private Double total;

    private Long tunjanganKomunikasi;

    private Long uangMakan;

    private Long tunjanganKhusus;

    private Long tunjanganVariable;
}
