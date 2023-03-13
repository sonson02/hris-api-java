package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.IuranMasterRequest;
import com.example.hrisapi.dto.response.IuranMasterResponse;
import com.example.hrisapi.entity.IuranMasterEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IuranMasterMapper {
    IuranMasterResponse map(IuranMasterEntity iuranMasterEntity);

    IuranMasterEntity mapRequest(IuranMasterRequest iuranMasterRequest);

}
