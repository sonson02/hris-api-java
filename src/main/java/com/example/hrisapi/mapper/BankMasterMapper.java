package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.BankMasterRequest;
import com.example.hrisapi.dto.response.BankMasterResponse;
import com.example.hrisapi.entity.BankMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMasterMapper {
    BankMasterResponse map(BankMasterEntity bankMasterEntity);

    BankMasterEntity mapRequest(BankMasterRequest bankMasterRequest);

}
