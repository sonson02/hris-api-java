package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KaryawanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KaryawanRepository extends JpaRepository<KaryawanEntity, UUID> {

    @Query(value = "select * from hrisnew.karyawan k where is_active = true", nativeQuery = true)
    List<KaryawanEntity> getKaryawanIsActive();

    KaryawanEntity findByKaryawanId(UUID karyawanId);

    KaryawanEntity findByKaryawanNip(String karyawanNip);
}
