package com.example.hrisapi.repository;

import com.example.hrisapi.entity.PajakMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PajakMasterRepository extends JpaRepository<PajakMasterEntity, UUID> {

    @Query(value = "select * from dbo.pajak_master pm where is_active = true", nativeQuery = true)
    List<PajakMasterEntity> getPajakMasterIsActive();

    PajakMasterEntity findByPajakId(UUID pajakId);
}
