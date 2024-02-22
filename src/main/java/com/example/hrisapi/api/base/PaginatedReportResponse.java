package com.example.hrisapi.api.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedReportResponse<T> {

  private int totalRecord;

  private int page;

  private int size;

  private List<T> data;

  private Double totalGaji;

  private Double totalTunjangan;

  private Double totalGajiDibayar;

  private Double totalManajemenFee;

  private Double totalTagihanGaji;

  private Double totalUangMakan;

  private Double totalTunjanganKhusus;

  private Double totalTunjanganVariabel;

  private Double totalTunjanganKomunikasi;

  private Double totalPph11;
}
