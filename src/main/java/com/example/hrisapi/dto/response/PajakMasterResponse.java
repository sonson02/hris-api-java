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
public class PajakMasterResponse {
    private UUID pajakId;

    private Date dtmUpdate;

    private Boolean isActive;

    private Double pajakPersen;

    private String pajakStatus;

    private String pajakType;

    private String usrUpdate;
}
