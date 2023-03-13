package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankMasterResponse {
    private UUID bankId;

    private String bankDesc;

    private String bankName;

    private Date dtmUpdate;

    private Boolean isActive;

    private String usrUpdate;
}
