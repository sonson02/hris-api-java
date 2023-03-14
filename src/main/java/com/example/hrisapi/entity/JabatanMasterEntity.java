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
@Table(name = "jabatan_master", schema = "dbo")
public class JabatanMasterEntity {

    @Id
    private UUID jabatanId;

    private Date dtmUpdate;

    private Boolean isActive;

    private String jabatanDesc;

    private String jabatanName;

    private Double tunjangan;

    private String usrUpdate;
}
