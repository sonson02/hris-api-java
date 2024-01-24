package com.example.hrisapi.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kontrak_kerja", schema = "dbo")
public class KontrakKerjaEntity {

    @Id
    private UUID kontrakId;

    private Date dtmUpdate;

    private Boolean isActive;

    private UUID karyawanId;

    private String karyawanNip;

    private String kontrakKode;

    private Integer periodKontrak;

    private String usrUpdate;

    private UUID fileUploadId;

    private String requestNo;

    private Date requestDate;

    private Long gaji;

    private UUID tempatTugasId;

    private Date tglHabisKontrak;

    private Date tglMasukKerja;

    private UUID unitId;

    private UUID jabatanId;

    private Long tunjanganKomunikasi;

    private Long uangMakan;

    private Long tunjangan;

    private Long tunjanganKhusus;

    private Long tunjanganVariable;
}
