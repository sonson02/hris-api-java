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
@Table(name = "pajak_master", schema = "hrisnew")
public class PajakMasterEntity {

    @Id
    private UUID pajakId;

    private Date dtmUpdate;

    private Boolean isActive;

    private String pajakPersen;

    private String pajakStatus;

    private String pajakType;

    private String usrUpdate;
}
