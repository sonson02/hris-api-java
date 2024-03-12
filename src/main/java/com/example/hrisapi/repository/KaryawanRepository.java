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

    @Query(value = "select * from dbo.kontrak_kerja kk " +
            "join dbo.karyawan k on kk.karyawan_nip = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and kk.unit_id = :unitId", nativeQuery = true)
    List<KaryawanEntity> getFilterKaryawanByUnitIdAndIsActive(@Param("unitId") UUID unitId);

    @Query(value = "select distinct on (1) * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tempat_tugas_id is not NULL",
            nativeQuery = true)
    List<KaryawanEntity> getKaryawanForReportGaji();

    @Query(value = "select * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "date_part('month', kk.tgl_masuk_kerja) = :bulan and " +
            "date_part('year', kk.tgl_masuk_kerja) = :tahun ",
            nativeQuery = true)
    List<KaryawanEntity> getKaryawanFilterByPeriode(@Param("bulan") Integer bulan, @Param("tahun") Integer tahun);

    @Query(value = "select * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() and now() + INTERVAL '30 day' "
            , nativeQuery = true)
    List<KaryawanEntity> getKaryawanDashboard30Days();

    @Query(value = "select * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() + INTERVAL '31 day' and now() + INTERVAL '60 day' "
            , nativeQuery = true)
    List<KaryawanEntity> getKaryawanDashboard60Days();

    @Query(value = "select * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() + INTERVAL '61 day' and now() + INTERVAL '90 day' "
            , nativeQuery = true)
    List<KaryawanEntity> getKaryawanDashboard90Days();

    @Query(value = "select * from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak between symmetric now() and now() + INTERVAL '90 day' " +
            "order by kk.tgl_habis_kontrak asc, kk.period_kontrak desc"
            , nativeQuery = true)
    List<KaryawanEntity> getKaryawanDashboardAll();

    @Query(value = "select count(*) from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() and now() + INTERVAL '30 day' "
            , nativeQuery = true)
    Integer getTotalKaryawanDashboard_30_Days();

    @Query(value = "select count(*) from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() + INTERVAL '31 day' and now() + INTERVAL '60 day' "
            , nativeQuery = true)
    Integer getTotalKaryawanDashboard_60_Days();

    @Query(value = "select count(*) from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = true and kk.is_active = true and " +
            "kk.tgl_habis_kontrak " +
            "between symmetric now() + INTERVAL '61 day' and now() + INTERVAL '90 day' "
            , nativeQuery = true)
    Integer getTotalKaryawanDashboard_90_Days();

    List<KaryawanEntity> findByKaryawanNameContainingIgnoreCaseAndIsActiveTrue(String name);

    @Query(value = "select count(*) from dbo.karyawan k where k.is_active = true "
            , nativeQuery = true)
    Integer getTotalKaryawanActive();

    @Query(value = "select count(*) from dbo.karyawan k " +
            "where k.is_active = true and " +
            "date_part('month', k.created_date) = date_part('month', now()) and " +
            "date_part('year', k.created_date) = date_part('year', now()) "
            , nativeQuery = true)
    Integer getTotalKaryawanBaru();

    @Query(value = "select count(*) from dbo.karyawan k " +
            "join dbo.kontrak_kerja kk on kk.karyawan_nip  = k.karyawan_nip " +
            "where k.is_active = false and kk.is_active = false and " +
            "date_part('month', kk.tgl_berhenti_kerja) = date_part('month', now()) and " +
            "date_part('year', kk.tgl_berhenti_kerja) = date_part('year', now()) "
            , nativeQuery = true)
    Integer getTotalKaryawanBerhenti();

}
