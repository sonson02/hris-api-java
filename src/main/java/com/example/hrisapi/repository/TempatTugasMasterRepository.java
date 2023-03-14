package com.example.hrisapi.repository;

import com.example.hrisapi.entity.TempatTugasMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TempatTugasMasterRepository extends JpaRepository<TempatTugasMasterEntity, UUID> {

    @Query(value = "select * from dbo.tempat_tugas_master ttm where is_active = true", nativeQuery = true)
    List<TempatTugasMasterEntity> getTempatTugasMasterIsActive();

    TempatTugasMasterEntity findByTempatTugasId(UUID tempatTugasId);
}
