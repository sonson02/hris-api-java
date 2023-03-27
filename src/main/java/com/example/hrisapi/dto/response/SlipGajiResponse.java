package com.example.hrisapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlipGajiResponse {

    private String karyawanNip;

    private String karyawanName;

    private String jabatanName;

    private String unitName;

    private String peiode;
}
