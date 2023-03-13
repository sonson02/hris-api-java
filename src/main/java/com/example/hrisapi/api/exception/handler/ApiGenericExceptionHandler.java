package com.example.hrisapi.api.exception.handler;

import com.example.hrisapi.api.base.JsonBaseError;
import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.ApiExceptionConstants;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiGenericExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ApiGenericExceptionHandler.class);

  @ExceptionHandler({ DataNotFoundException.class })
  public ResponseEntity<JsonBaseResponse<String>> handleDataNotExistException(DataNotFoundException e) {
    var startTime = System.currentTimeMillis();

    var genericError = JsonBaseError
            .builder()
            .code(ApiExceptionConstants.CODE_PROCESSING_ERROR)
            .message(ApiExceptionConstants.CAUSE_DATA_NOT_FOUND)
            .cause(ApiExceptionConstants.CAUSE_DATA_NOT_FOUND)
            .build();
    var body = new JsonBaseResponse<String>(startTime, genericError);

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(body);
  }
}
