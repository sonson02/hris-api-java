package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.BankMasterRequest;
import com.example.hrisapi.dto.response.BankMasterResponse;
import com.example.hrisapi.entity.BankMasterEntity;
import com.example.hrisapi.mapper.BankMasterMapper;
import com.example.hrisapi.repository.BankMasterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BankMasterService {

    @Autowired
    private BankMasterRepository bankMasterRepository;

    @Autowired
    private BankMasterMapper bankMasterMapper;

    public PaginatedResponse<BankMasterResponse> getListBankMaster(Integer page, Integer size){
        List<BankMasterEntity> listBankEntity = bankMasterRepository.getBankMasterIsActive();
        List<BankMasterResponse> listBankResponse = new ArrayList<>();

        for(BankMasterEntity bme : listBankEntity){
            BankMasterResponse response = bankMasterMapper.map(bme);
            listBankResponse.add(response);
        }

        return (PaginatedResponse<BankMasterResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listBankResponse
        );
    }

    @Transactional
    public BankMasterResponse insertBankMaster(BankMasterRequest request){
        BankMasterEntity bme = bankMasterMapper.mapRequest(request);
        bme.setBankId(UUID.randomUUID());
        bme.setIsActive(true);
        bankMasterRepository.save(bme);

        BankMasterResponse response = bankMasterMapper.map(bme);

        return response;
    }

    @Transactional
    public BankMasterResponse updateBankMaster(BankMasterRequest request){

        BankMasterEntity bmeExist = bankMasterRepository.findByBankId(request.getBankId());

        if(bmeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, bmeExist);
        bmeExist.setDtmUpdate(new Date());
        bankMasterRepository.save(bmeExist);

        BankMasterResponse response = bankMasterMapper.map(bmeExist);

        return response;
    }
}
