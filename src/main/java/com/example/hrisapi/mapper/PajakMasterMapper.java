package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.PajakMasterRequest;
import com.example.hrisapi.dto.response.PajakMasterResponse;
import com.example.hrisapi.entity.PajakMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PajakMasterMapper {
    PajakMasterResponse map(PajakMasterEntity pajakMasterEntity);

    PajakMasterEntity mapRequest(PajakMasterRequest pajakMasterRequest);

}
