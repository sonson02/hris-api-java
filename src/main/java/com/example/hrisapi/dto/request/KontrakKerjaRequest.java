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

    private String kontrakKode;

    private String requestNo;

    private Long gaji;

    private Date tglMasukKerja;

    private Date tglHabisKontrak;

    private UUID tempatTugasId;

    private UUID unitId;

    private UUID jabatanId;

    private Boolean isActive;

    private String usrUpdate;

    private Date requestDate;

    private Long tunjanganKomunikasi;

    private Long uangMakan;

    private Long tunjangan;

    private Long tunjanganKhusus;

    private Long tunjanganVariable;

    private String uploadDocKontrak;

    private Boolean isUpload;
}
