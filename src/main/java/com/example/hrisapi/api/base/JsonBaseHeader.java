package com.example.hrisapi.api.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for JSON element on response body, section <code>header</code>.
 *
 * @author timpamungkas
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonBaseHeader {

  @Builder.Default
  private List<JsonBaseError> errors = new ArrayList<>();

  /**
   * Process time to generate the entire response
   */
  private long processTime;

  /**
   * <code>true</code> if your API response is returning HTTP status response 2xx,
   * <code>false</code> otherwise.
   */
  private boolean success;

  public void addError(JsonBaseError error) {
    this.errors.add(error);
  }
}
