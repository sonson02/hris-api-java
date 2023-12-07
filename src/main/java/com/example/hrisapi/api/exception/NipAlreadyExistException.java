package com.example.hrisapi.api.exception;

import com.example.hrisapi.constant.ApiExceptionConstants;

public class NipAlreadyExistException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Override
  public String getLocalizedMessage() {
    return ApiExceptionConstants.NIP_ALREADY_EXIST;
  }
}
