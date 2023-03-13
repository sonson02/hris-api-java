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
@Table(name = "kontrak_kerja", schema = "hrisnew")
public class KontrakKerjaEntity {

    @Id
    private UUID kontrakId;

    private Date dtmUpdate;

    private Date endKontrak;

    private Boolean isActive;

    private String karyawanNip;

    private String kontrakKode;

    private String periodKontrak;

    private Date startKontrak;

    private UUID tempatTugasId;

    private String usrUpdate;
}
