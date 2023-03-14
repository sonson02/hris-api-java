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
public class KaryawanByNipResponse {
    private String karyawanNip;

    private String karyawanName;

    private String nonik;

    private String tempatTinggal;

    private Date tanggalLahir;
}
