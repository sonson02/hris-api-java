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
public class UnitMasterResponse {
    private UUID unitId;

    private Date dtmUpdate;

    private Boolean isActive;

    private String unitDescription;

    private String unitName;

    private String usrUpdate;
}
