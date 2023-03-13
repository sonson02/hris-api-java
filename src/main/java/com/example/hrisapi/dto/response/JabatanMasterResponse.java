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
public class JabatanMasterResponse {
    private UUID jabatanId;

    private Date dtmUpdate;

    private Boolean isActive;

    private String jabatanDesc;

    private String jabatanName;

    private Double tunjangan;

    private String usrUpdate;
}
