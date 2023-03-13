package com.example.hrisapi.api.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper class for JSON element on response body, section
 * <code>header.errors</code>.
 *
 * @author timpamungkas
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonBaseError {

  /**
   * Custom error code
   */
  private String code;

  /**
   * User-friendly error message to be displayed on frontend.
   */
  private String message;

  /**
   * Technical error message for debugging.
   */
  private String cause;
}
