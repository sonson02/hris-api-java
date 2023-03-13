package com.example.hrisapi.api.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Pagination wrapper
 *
 * @author timpamungkas
 *
 * @param <T> data type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonBasePage<T> {

  private int size;

  private int page;

  private int totalPage;

  private long totalRecord;

  private List<T> data;
}
