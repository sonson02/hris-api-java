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
@Table(name = "tempat_tugas_master", schema = "dbo")
public class TempatTugasMasterEntity {

    @Id
    private UUID tempatTugasId;

    private Date dtmUpdate;

    private Boolean isActive;

    private UUID jabatanId;

    private String lokasiTempatTugas;

    private String namaProyek;

    private UUID unitId;

    private String usrUpdate;
}
