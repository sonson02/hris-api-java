package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.response.JabatanMasterResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JabatanMasterMapper {
    JabatanMasterResponse map(JabatanMasterEntity jabatanMasterEntity);

    JabatanMasterEntity mapRequest(JabatanMasterRequest jabatanMasterRequest);

}
