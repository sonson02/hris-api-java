package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.request.UnitMasterRequest;
import com.example.hrisapi.dto.response.JabatanMasterResponse;
import com.example.hrisapi.dto.response.UnitMasterResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.entity.UnitMasterEntity;
import com.example.hrisapi.mapper.JabatanMasterMapper;
import com.example.hrisapi.mapper.UnitMasterMapper;
import com.example.hrisapi.repository.JabatanMasterRepository;
import com.example.hrisapi.repository.UnitMasterRepository;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UnitMasterService {

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    @Autowired
    private UnitMasterMapper unitMasterMapper;

    public PaginatedResponse<UnitMasterResponse> getListUnitMaster(Integer page, Integer size){
        List<UnitMasterEntity> listUnitEntity = unitMasterRepository.getUnitMasterIsActive();
        List<UnitMasterResponse> listUnitResponse = new ArrayList<>();

        for(UnitMasterEntity ume : listUnitEntity){
            UnitMasterResponse response = unitMasterMapper.map(ume);
            listUnitResponse.add(response);
        }

        return (PaginatedResponse<UnitMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listUnitResponse
        );
    }

    @Transactional
    public UnitMasterResponse insertUnitMaster(UnitMasterRequest request){
        UnitMasterEntity ume = unitMasterMapper.mapRequest(request);
        ume.setUnitId(UUID.randomUUID());
        ume.setIsActive(true);
        unitMasterRepository.save(ume);

        UnitMasterResponse response = unitMasterMapper.map(ume);

        return response;
    }

    @Transactional
    public UnitMasterResponse updateUnitMaster(UnitMasterRequest request){

        UnitMasterEntity umeExist = unitMasterRepository.findByUnitId(request.getUnitId());

        if(umeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, umeExist);
        umeExist.setDtmUpdate(new Date());
        unitMasterRepository.save(umeExist);

        UnitMasterResponse response = unitMasterMapper.map(umeExist);

        return response;
    }
}
