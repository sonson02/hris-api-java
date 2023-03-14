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
@Table(name = "pkwt_kontrak", schema = "dbo")
public class PkwtKontrakEntity {

    @Id
    private UUID pkwtFileId;

    private Date dtmSigned;

    private String kontrakFileSigned;

    private UUID kontrakId;
}
