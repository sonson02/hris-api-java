package com.example.hrisapi.repository;

import com.example.hrisapi.entity.IuranMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IuranMasterRepository extends JpaRepository<IuranMasterEntity, UUID> {

    @Query(value = "select * from dbo.iuran_master im where is_active = true", nativeQuery = true)
    List<IuranMasterEntity> getIuranMasterIsActive();

    IuranMasterEntity findByIuranId(UUID iuranId);
}
