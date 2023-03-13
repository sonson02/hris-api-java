package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.request.UnitMasterRequest;
import com.example.hrisapi.dto.response.UnitMasterResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.entity.UnitMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMasterMapper {
    UnitMasterResponse map(UnitMasterEntity unitMasterEntity);

    UnitMasterEntity mapRequest(UnitMasterRequest unitMasterRequest);

}
