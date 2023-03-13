package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.TempatTugasMasterRequest;
import com.example.hrisapi.dto.response.TempatTugasMasterResponse;
import com.example.hrisapi.entity.TempatTugasMasterEntity;
import com.example.hrisapi.mapper.TempatTugasMasterMapper;
import com.example.hrisapi.repository.TempatTugasMasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TempatTugasMasterService {

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private TempatTugasMasterMapper tempatTugasMasterMapper;

    public PaginatedResponse<TempatTugasMasterResponse> getListTempatTugasMaster(Integer page, Integer size){
        List<TempatTugasMasterEntity> listTempatTugasEntity = tempatTugasMasterRepository.getTempatTugasMasterIsActive();
        List<TempatTugasMasterResponse> listTempatTugasResponse = new ArrayList<>();

        for(TempatTugasMasterEntity ttme : listTempatTugasEntity){
            TempatTugasMasterResponse response = tempatTugasMasterMapper.map(ttme);
            listTempatTugasResponse.add(response);
        }

        return (PaginatedResponse<TempatTugasMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listTempatTugasResponse
        );
    }

    @Transactional
    public TempatTugasMasterResponse insertTempatTugasMaster(TempatTugasMasterRequest request){
        TempatTugasMasterEntity ttme = tempatTugasMasterMapper.mapRequest(request);
        ttme.setTempatTugasId(UUID.randomUUID());
        ttme.setDtmUpdate(new Date());
        ttme.setIsActive(true);
        tempatTugasMasterRepository.save(ttme);

        TempatTugasMasterResponse response = tempatTugasMasterMapper.map(ttme);

        return response;
    }

    @Transactional
    public TempatTugasMasterResponse updateTempatTugasMaster(TempatTugasMasterRequest request){

        TempatTugasMasterEntity ttmeExist = tempatTugasMasterRepository.findByTempatTugasId(request.getTempatTugasId());

        if(ttmeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, ttmeExist);
        ttmeExist.setDtmUpdate(new Date());
        tempatTugasMasterRepository.save(ttmeExist);

        TempatTugasMasterResponse response = tempatTugasMasterMapper.map(ttmeExist);

        return response;
    }
}
