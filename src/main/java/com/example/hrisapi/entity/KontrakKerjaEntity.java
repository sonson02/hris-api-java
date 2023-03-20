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
}
