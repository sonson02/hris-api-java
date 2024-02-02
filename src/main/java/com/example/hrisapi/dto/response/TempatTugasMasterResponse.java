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
public class TempatTugasMasterResponse {
    private UUID tempatTugasId;

    private Date dtmUpdate;

    private Boolean isActive;

    private UUID jabatanId;

    private String lokasiTempatTugas;

    private String namaProyek;

    private UUID unitId;

    private String usrUpdate;
}
