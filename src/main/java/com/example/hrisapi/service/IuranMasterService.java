package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.IuranMasterRequest;
import com.example.hrisapi.dto.response.IuranMasterResponse;
import com.example.hrisapi.entity.IuranMasterEntity;
import com.example.hrisapi.mapper.IuranMasterMapper;
import com.example.hrisapi.repository.IuranMasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class IuranMasterService {

    @Autowired
    private IuranMasterRepository iuranMasterRepository;

    @Autowired
    private IuranMasterMapper iuranMasterMapper;

    public PaginatedResponse<IuranMasterResponse> getListIuranMaster(Integer page, Integer size){
        List<IuranMasterEntity> listIuranEntity = iuranMasterRepository.getIuranMasterIsActive();
        List<IuranMasterResponse> listIuranResponse = new ArrayList<>();

        for(IuranMasterEntity ime : listIuranEntity){
            IuranMasterResponse response = iuranMasterMapper.map(ime);
            listIuranResponse.add(response);
        }

        return (PaginatedResponse<IuranMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listIuranResponse
        );
    }

    @Transactional
    public IuranMasterResponse insertIuranMaster(IuranMasterRequest request){
        IuranMasterEntity ime = iuranMasterMapper.mapRequest(request);
        ime.setIuranId(UUID.randomUUID());
        ime.setDtmUpdate(new Date());
        ime.setIsActive(true);
        iuranMasterRepository.save(ime);

        IuranMasterResponse response = iuranMasterMapper.map(ime);

        return response;
    }

    @Transactional
    public IuranMasterResponse updateIuranMaster(IuranMasterRequest request){

        IuranMasterEntity imeExist = iuranMasterRepository.findByIuranId(request.getIuranId());

        if(imeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, imeExist);
        imeExist.setDtmUpdate(new Date());
        iuranMasterRepository.save(imeExist);

        IuranMasterResponse response = iuranMasterMapper.map(imeExist);

        return response;
    }
}
