package com.example.hrisapi.repository;

import com.example.hrisapi.entity.HistoryGajiEntity;
import com.example.hrisapi.entity.KaryawanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface HistoryGajiRepository extends JpaRepository<HistoryGajiEntity, UUID> {

    @Query(value = "select * from dbo.history_gaji hg " +
            "where karyawan_nip = :karyawanNip "
            , nativeQuery = true)
    List<HistoryGajiEntity> getKaryawanNipHistoryGaji(@Param("karyawanNip") String karyawanNip);
}
