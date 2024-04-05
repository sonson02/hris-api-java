package com.example.hrisapi.repository;

import com.example.hrisapi.entity.MasterPph21Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MasterPph21Repository extends JpaRepository<MasterPph21Entity, UUID> {

    @Query(value = "select * from dbo.master_pph21 mp " +
            "where mp.pph21_jenis = :jenis and " +
            ":gaji between mp.pph21_batas_bawah and mp.pph21_batas_atas ",
            nativeQuery = true)
    MasterPph21Entity getNominalTer(@Param("jenis") String jenis, @Param("gaji") Double gaji);

}
