package com.example.hrisapi.repository;

import com.example.hrisapi.entity.BankMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankMasterRepository extends JpaRepository<BankMasterEntity, UUID> {

    @Query(value = "select * from hrisnew.bank_master bm where is_active = true", nativeQuery = true)
    List<BankMasterEntity> getBankMasterIsActive();

    BankMasterEntity findByBankId(UUID bankId);
}
