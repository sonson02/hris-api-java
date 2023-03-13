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
@Table(name = "bank_master", schema = "hrisnew")
public class BankMasterEntity {

    @Id
    private UUID bankId;

    private String bankDesc;

    private String bankName;

    private Date dtmUpdate;

    private Boolean isActive;

    private String usr_Update;
}
