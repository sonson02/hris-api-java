package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KontrakKerjaByNipResponse {
    private UUID kontrakId;

    private String karyawanNip;

    private String kontrakKode;

    private String karyawanName;

    private UUID unitId;

    private String unitName;

    private String tglHabisKontrak;

    private String periodKontrak;

    private Boolean isActive;
}
