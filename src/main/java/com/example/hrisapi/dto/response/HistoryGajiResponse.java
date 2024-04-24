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
public class HistoryGajiResponse {

    private UUID historyGajiId;

    private String karyawanNip;

    private Integer bulan;

    private Integer tahun;

    private Double gaji;

    private Double tunjanganKomunikasi;

    private Double uangMakan;

    private Double tunjangan;

    private Double tunjanganKhusus;

    private Double tunjanganVariable;

    private String dtmUpdate;

    private String usrUpdate;
}
