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
public class PaginatedReportJamsosResponse<T> {

  private int totalRecord;

  private int page;

  private int size;

  private List<T> data;

  private Double totalGaji;

  private Double totalGajiTambahUangMakan;

  private Double totalBpjsKkJkk;

  private Double totalBpjsKkJkm;

  private Double totalBpjsKkJht;

  private Double totalBpjsKkJkkJkmJht;

  private Double totalBpjsKkBebanPegawai;

  private Double totalBpjsKsBebanPerusahaan;

  private Double totalBpjsKsBebanPegawai;

  private Double totalBpjsTkBebanPerusahaan;

  private Double totalBpjsTkBebanPegawai;

  private Double totalPphPasal21;

  private Double totalGajiDiterima;

  private Double subTotalA;

  private Double managementFee5;

  private Double subTotalAB;

  private Double ppn11;

  private Double grandTotal;

  private String terbilang;


}
