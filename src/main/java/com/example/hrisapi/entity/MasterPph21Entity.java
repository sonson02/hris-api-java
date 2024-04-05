package com.example.hrisapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "master_pph21", schema = "dbo")
public class MasterPph21Entity {

    @Id
    @Column(name = "pph21_id")
    private UUID pph21Id;

    @Column(name = "pph21_jenis")
    private String pph21Jenis;

    @Column(name = "pph21_batas_bawah")
    private Double pph21BatasBawah;

    @Column(name = "pph21_batas_atas")
    private Double pph21BatasAtas;

    @Column(name = "pph21_ter")
    private Double pph21Ter;
}
