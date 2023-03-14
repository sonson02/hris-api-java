package com.example.hrisapi.mapper;

import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.response.KaryawanByNipResponse;
import com.example.hrisapi.dto.response.KaryawanResponse;
import com.example.hrisapi.dto.response.ListKaryawanResponse;
import com.example.hrisapi.entity.KaryawanEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KaryawanMapper {
    KaryawanResponse map(KaryawanEntity karyawanEntity);

    KaryawanEntity mapRequest(KaryawanRequest karyawanRequest);

    KaryawanByNipResponse mapNip(KaryawanEntity karyawanEntity);

    ListKaryawanResponse mapList(KaryawanEntity karyawanEntity);
}
