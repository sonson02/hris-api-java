package com.example.hrisapi.repository;

import com.example.hrisapi.entity.JabatanMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JabatanMasterRepository extends JpaRepository<JabatanMasterEntity, UUID> {

    @Query(value = "select * from dbo.jabatan_master jm where is_active = true", nativeQuery = true)
    List<JabatanMasterEntity> getJabatanMasterIsActive();

    JabatanMasterEntity findByJabatanId(UUID jabatanId);
}
