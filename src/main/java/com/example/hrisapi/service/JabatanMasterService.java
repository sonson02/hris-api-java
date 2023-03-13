package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.response.JabatanMasterResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.mapper.JabatanMasterMapper;
import com.example.hrisapi.repository.JabatanMasterRepository;
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
public class JabatanMasterService {

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private JabatanMasterMapper jabatanMasterMapper;

    public PaginatedResponse<JabatanMasterResponse> getListJabatanMaster(Integer page, Integer size){
        List<JabatanMasterEntity> listJabatanEntity = jabatanMasterRepository.getJabatanMasterIsActive();
        List<JabatanMasterResponse> listJabatanResponse = new ArrayList<>();

        for(JabatanMasterEntity jme : listJabatanEntity){
            JabatanMasterResponse response = jabatanMasterMapper.map(jme);
            listJabatanResponse.add(response);
        }

        return (PaginatedResponse<JabatanMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listJabatanResponse
        );
    }

    @Transactional
    public JabatanMasterResponse insertJabatanMaster(JabatanMasterRequest request){
        JabatanMasterEntity jme = jabatanMasterMapper.mapRequest(request);
        jme.setJabatanId(UUID.randomUUID());
        jme.setIsActive(true);
        jabatanMasterRepository.save(jme);

        JabatanMasterResponse response = jabatanMasterMapper.map(jme);

        return response;
    }

    @Transactional
    public JabatanMasterResponse updateJabatanMaster(JabatanMasterRequest request){

        JabatanMasterEntity jmeExist = jabatanMasterRepository.findByJabatanId(request.getJabatanId());

        if(jmeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, jmeExist);
        jmeExist.setDtmUpdate(new Date());
        jabatanMasterRepository.save(jmeExist);

        JabatanMasterResponse response = jabatanMasterMapper.map(jmeExist);

        return response;
    }
}
