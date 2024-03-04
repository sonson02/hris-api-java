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
public class PaginatedDashboardResponse<T> {

  private int totalRecord;

  private int page;

  private int size;

  private List<T> data;

  private Integer total30Days;

  private Integer total60Days;

  private Integer total90Days;

  private Integer totalKaryawanActive;

  private Integer totalKaryawanBaru;

  private Integer totalKaryawanBerhenti;

}
