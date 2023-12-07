package com.example.hrisapi.api.exception;

import com.example.hrisapi.constant.ApiExceptionConstants;

public class NikAlreadyExistException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Override
  public String getLocalizedMessage() {
    return ApiExceptionConstants.NIK_ALREADY_EXIST;
  }
}
