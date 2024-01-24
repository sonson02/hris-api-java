package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.KontrakKerjaRequest;
import com.example.hrisapi.dto.response.KontrakKerjaByNipResponse;
import com.example.hrisapi.dto.response.KontrakKerjaResponse;
import com.example.hrisapi.entity.KontrakKerjaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KontrakKerjaMapper {

    KontrakKerjaByNipResponse mapByNip(KontrakKerjaEntity kontrakKerjaEntity);
}
