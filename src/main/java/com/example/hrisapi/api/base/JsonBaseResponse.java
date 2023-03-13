package com.example.hrisapi.api.base;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper class for JSON element on response body. Use your own class as
 * generic type replacement.
 *
 * @author timpamungkas
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonBaseResponse<T> {

  /**
   * <code>header</code> section
   */
  private JsonBaseHeader header;

  /**
   * Actual data
   */
  private T data;

  /**
   * Return no data, should be called if your API returns non 2xx response code
   *
   * @param startTime processing start time millis
   * @param errors
   */
  public JsonBaseResponse(long startTime, JsonBaseError... errors) {
    this(startTime, Arrays.asList(errors));
  }

  /**
   * Return no data, should be called if your API returns non 2xx response code
   *
   * @param startTime processing start time millis
   * @param errors
   */
  public JsonBaseResponse(long startTime, List<JsonBaseError> errors) {
    this();
    var jsonBaseHeader = new JsonBaseHeader();
    jsonBaseHeader.setSuccess(false);
    jsonBaseHeader.setErrors(errors);
    jsonBaseHeader.setProcessTime(System.currentTimeMillis() - startTime);
    this.setHeader(jsonBaseHeader);
  }

  /**
   *
   * @param data      data to be returned
   * @param startTime processing start time millis
   */
  public JsonBaseResponse(long startTime, T data) {
    this();
    var jsonBaseHeader = new JsonBaseHeader();
    jsonBaseHeader.setSuccess(true);
    jsonBaseHeader.setErrors(new ArrayList<>());
    jsonBaseHeader.setProcessTime(System.currentTimeMillis() - startTime);
    this.setHeader(jsonBaseHeader);

    this.setData(data);
  }
}
