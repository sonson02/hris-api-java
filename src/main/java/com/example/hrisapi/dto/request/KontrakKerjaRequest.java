package com.example.hrisapi.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Validated
public class KontrakKerjaRequest {

    private UUID kontrakId;

    private String karyawanNip;

    private String nonik;

    private UUID unitId;

    private UUID tempatTugasId;

    private UUID jabatanId;

    private Double gaji;

    private String karyawanName;

    private String tempatTinggal;

    private Date tanggalLahir;

    private String tipeTunjangan;

    private Date tglMasukKerja;

    private Date tglHabisKontrak;

    private String kontrakKode;

    private Boolean isActive;

    private String usrUpdate;

    private String requestNo;

    private Date requestDate;

    private Double uangMakan;
}
