package com.example.hrisapi.repository;

import com.example.hrisapi.entity.UnitMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UnitMasterRepository extends JpaRepository<UnitMasterEntity, UUID> {

    @Query(value = "select * from hrisnew.unit_master um where is_active = true", nativeQuery = true)
    List<UnitMasterEntity> getUnitMasterIsActive();

    UnitMasterEntity findByUnitId(UUID unitId);
}
