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
@Table(name = "unit_master", schema = "hrisnew")
public class UnitMasterEntity {

    @Id
    private UUID unitId;

    private Date dtmUpdate;

    private Boolean isActive;

    private String unitDescription;

    private String unitName;

    private String usrUpdate;
}
