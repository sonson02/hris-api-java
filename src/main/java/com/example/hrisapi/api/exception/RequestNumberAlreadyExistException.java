package com.example.hrisapi.api.exception;

import com.example.hrisapi.constant.ApiExceptionConstants;

public class RequestNumberAlreadyExistException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Override
  public String getLocalizedMessage() {
    return ApiExceptionConstants.REQUEST_NUMBER_ALREADY_EXIST;
  }
}
