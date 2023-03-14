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
public class ListKaryawanResponse {
    private UUID karyawanId;

    private String karyawanNip;

    private String karyawanName;

    private String namaProyek;

    private String unitName;

    private String jabatanName;

    private Boolean isActive;

    private Date tglHabisKontrak;

    private String periodKontrak;
}
