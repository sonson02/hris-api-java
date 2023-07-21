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
public class PaginatedSlipGajiByNipResponse<T> {

  private int totalRecord;

  private List<T> data;
}
