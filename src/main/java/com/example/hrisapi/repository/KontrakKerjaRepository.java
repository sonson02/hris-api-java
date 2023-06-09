package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KaryawanEntity;
import com.example.hrisapi.entity.KontrakKerjaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KontrakKerjaRepository extends JpaRepository<KontrakKerjaEntity, UUID> {

    @Query(value = "select * from dbo.kontrak_kerja kk where is_active = true", nativeQuery = true)
    List<KontrakKerjaEntity> getKontrakKerjaIsActive();

    KontrakKerjaEntity findByKontrakId(UUID kontrakId);

    List<KontrakKerjaEntity> findByKaryawanNip(String karyawanNip);

    @Query(value = "select count(*) from dbo.kontrak_kerja kk where karyawan_nip = :karyawanNip", nativeQuery = true)
    int getCountPeriodKontrak(@Param("karyawanNip") String karyawanNip);

    @Query(value = "select max(period_kontrak) from dbo.kontrak_kerja kk where karyawan_nip = :karyawanNip", nativeQuery = true)
    int getMaxPeriodKontrakForListKaryawan(@Param("karyawanNip") String karyawanNip);

    @Query(value = "select kk.kontrak_id, kk.dtm_update ," +
            "kk.is_active, kk.karyawan_id, kk.karyawan_nip , " +
            "kk.kontrak_kode , kk.period_kontrak , kk.usr_update ," +
            "kk.file_upload_id, kk.request_no, kk.request_date " +
            "from dbo.kontrak_kerja kk " +
            "join dbo.karyawan k on kk.karyawan_nip = k.karyawan_nip " +
            "where k.is_active = true " +
            "and k.unit_id = :unitId",
            nativeQuery = true)
    List<KontrakKerjaEntity> getFilterKontrakByUnitIdAndIsActive(@Param("unitId") UUID unitId);

    @Query(value = "select kk.kontrak_id, kk.dtm_update ," +
            "kk.is_active, kk.karyawan_id, kk.karyawan_nip , " +
            "kk.kontrak_kode , kk.period_kontrak , kk.usr_update ," +
            "kk.file_upload_id, kk.request_no, kk.request_date " +
            "from dbo.kontrak_kerja kk " +
            "join dbo.karyawan k on kk.karyawan_nip = k.karyawan_nip " +
            "where k.is_active = true " +
            "and k.karyawan_name ILIKE %:karyawanName% ",
            nativeQuery = true)
    List<KontrakKerjaEntity> getFilterKontrakByKaryawanNameAndIsActive(@Param("karyawanName") String name);
}
