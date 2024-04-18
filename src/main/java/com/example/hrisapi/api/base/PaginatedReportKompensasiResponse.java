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
public class PaginatedReportKompensasiResponse<T> {
  private int totalRecord;

  private int page;

  private int size;

  private List<T> data;

  private Double totalKompensasiDiterima;

  private Double totalManagementFee;

  private Double totalTotal;
}
