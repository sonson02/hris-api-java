package com.example.hrisapi.api.exception;

import com.example.hrisapi.constant.ApiExceptionConstants;

public class ContractStillActiveException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  @Override
  public String getLocalizedMessage() {
    return ApiExceptionConstants.CONTRACT_STILL_ACTIVE;
  }
}
