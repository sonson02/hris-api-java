package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.PajakMasterRequest;
import com.example.hrisapi.dto.response.PajakMasterResponse;
import com.example.hrisapi.entity.PajakMasterEntity;
import com.example.hrisapi.mapper.PajakMasterMapper;
import com.example.hrisapi.repository.PajakMasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PajakMasterService {

    @Autowired
    private PajakMasterRepository pajakMasterRepository;

    @Autowired
    private PajakMasterMapper pajakMasterMapper;

    public PaginatedResponse<PajakMasterResponse> getListPajakMaster(Integer page, Integer size){
        List<PajakMasterEntity> listPajakEntity = pajakMasterRepository.getPajakMasterIsActive();
        List<PajakMasterResponse> listUnitResponse = new ArrayList<>();

        for(PajakMasterEntity pme : listPajakEntity){
            PajakMasterResponse response = pajakMasterMapper.map(pme);
            listUnitResponse.add(response);
        }

        return (PaginatedResponse<PajakMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listUnitResponse
        );
    }

    @Transactional
    public PajakMasterResponse insertPajakMaster(PajakMasterRequest request){
        PajakMasterEntity pme = pajakMasterMapper.mapRequest(request);
        pme.setPajakId(UUID.randomUUID());
        pme.setIsActive(true);
        pajakMasterRepository.save(pme);

        PajakMasterResponse response = pajakMasterMapper.map(pme);

        return response;
    }

    @Transactional
    public PajakMasterResponse updatePajakMaster(PajakMasterRequest request){

        PajakMasterEntity pmeExist = pajakMasterRepository.findByPajakId(request.getPajakId());

        if(pmeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, pmeExist);
        pmeExist.setDtmUpdate(new Date());
        pajakMasterRepository.save(pmeExist);

        PajakMasterResponse response = pajakMasterMapper.map(pmeExist);

        return response;
    }
}
