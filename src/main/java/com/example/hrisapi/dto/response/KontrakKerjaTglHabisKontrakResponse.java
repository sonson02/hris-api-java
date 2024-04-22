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
public class KontrakKerjaTglHabisKontrakResponse {
    private UUID kontrakId;

    private String karyawanNip;

    private String karyawanName;

    private String unitName;

    private String jabatanName;

    private String namaProyek;

    private String tglHabisKontrak;

    private Integer periodKontrak;

    private Boolean isActive;
}
