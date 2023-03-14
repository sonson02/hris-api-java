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
@Table(name = "iuran_master", schema = "dbo")
public class IuranMasterEntity {

    @Id
    private UUID iuranId;

    private Date dtmUpdate;

    private Boolean isActive;

    private Double iuranBeban;

    private Double iuranPersen;

    private String iuranType;

    private String usrUpdate;
}
