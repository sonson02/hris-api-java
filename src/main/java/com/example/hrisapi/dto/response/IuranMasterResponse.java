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
public class IuranMasterResponse {
    private UUID iuranId;

    private Date dtmUpdate;

    private Boolean isActive;

    private Double iuranBeban;

    private Double iuranPersen;

    private String iuranType;

    private String usrUpdate;
}
