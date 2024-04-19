package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KontrakKerjaHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KontrakKerjaHistoryRepository extends JpaRepository<KontrakKerjaHistoryEntity, UUID> {

}
