package com.example.hrisapi.repository;

import com.example.hrisapi.entity.KaryawanHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KaryawanHistoryRepository extends JpaRepository<KaryawanHistoryEntity, UUID> {

}
