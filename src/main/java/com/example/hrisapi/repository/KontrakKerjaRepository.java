package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KontrakKerjaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KontrakKerjaRepository extends JpaRepository<KontrakKerjaEntity, UUID> {

    @Query(value = "select * from dbo.kontrak_kerja kk where is_active = true", nativeQuery = true)
    List<KontrakKerjaEntity> getKontrakKerjaIsActive();

    KontrakKerjaEntity findByKontrakId(UUID kontrakId);

    KontrakKerjaEntity findByKaryawanNip(String karyawanNip);
}
