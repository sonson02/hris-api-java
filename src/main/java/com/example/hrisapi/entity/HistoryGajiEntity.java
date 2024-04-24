package com.example.hrisapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "history_gaji", schema = "dbo")
public class HistoryGajiEntity {

    @Id
    private UUID historyGajiId;

    private String karyawanNip;

    private Integer bulan;

    private Integer tahun;

    private Double gaji;

    private Double tunjanganKomunikasi;

    private Double uangMakan;

    private Double tunjangan;

    private Double tunjanganKhusus;

    private Double tunjanganVariable;

    private Date dtmUpdate;

    private String usrUpdate;
}
