package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KaryawanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KaryawanRepository extends JpaRepository<KaryawanEntity, UUID> {

    @Query(value = "select * from dbo.karyawan k where is_active = true", nativeQuery = true)
    List<KaryawanEntity> getKaryawanIsActive();

    KaryawanEntity findByKaryawanId(UUID karyawanId);

    KaryawanEntity findByKaryawanNip(String karyawanNip);

    @Query(value = "select * from dbo.karyawan k where is_active = true and karyawan_nip = :karyawanNip", nativeQuery = true)
    KaryawanEntity getFilterKaryawanNipAndIsActive(@Param("karyawanNip") String karyawanNip);

    @Query(value = "select * from dbo.karyawan k where is_active = true and unit_id = :unitId", nativeQuery = true)
    List<KaryawanEntity> getFilterKaryawanByUnitIdAndIsActive(@Param("unitId") UUID unitId);

    @Query(value = "select * from dbo.karyawan k where is_active = true and tempat_tugas_id is not NULL", nativeQuery = true)
    List<KaryawanEntity> getKaryawanForReportGaji();

    @Query(value = "select * from dbo.karyawan k where is_active = true and date_part('month', tgl_masuk_kerja) = :bulan and date_part('year', tgl_masuk_kerja) = :tahun ", nativeQuery = true)
    List<KaryawanEntity> getKaryawanFilterByPeriode(@Param("bulan") Integer bulan, @Param("tahun") Integer tahun);
}
