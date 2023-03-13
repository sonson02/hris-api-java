package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.response.KaryawanResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.mapper.KaryawanMapper;
import com.example.hrisapi.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class KaryawanService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private KaryawanMapper karyawanMapper;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    public PaginatedResponse<KaryawanResponse> getListKaryawan(String nip, String unitName, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = karyawanRepository.getKaryawanIsActive();
        List<KaryawanResponse> listKaryawanResponse = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity){
            KaryawanResponse response = karyawanMapper.map(ke);

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(ke.getTempatTugasId());
            response.setNamaProyek(ttme.getNamaProyek());

            UnitMasterEntity ume = unitMasterRepository.findByUnitId(ke.getUnitId());
            response.setUnitName(ume.getUnitName());

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(ke.getJabatanId());
            response.setJabatanName(jme.getJabatanName());

            KontrakKerjaEntity kk = kontrakKerjaRepository.findByKaryawanNip(ke.getKaryawanNip());
            response.setPeriodKontrak(kk.getPeriodKontrak());

            listKaryawanResponse.add(response);
        }

        return (PaginatedResponse<KaryawanResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKaryawanResponse
        );
    }

    @Transactional
    public KaryawanResponse insertKaryawan(KaryawanRequest request){
        KaryawanEntity ke = karyawanMapper.mapRequest(request);
        ke.setKaryawanId(UUID.randomUUID());
        ke.setIsActive(true);
        karyawanRepository.save(ke);

        KaryawanResponse response = karyawanMapper.map(ke);

        return response;
    }

    @Transactional
    public KaryawanResponse updateKaryawan(KaryawanRequest request){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanId(request.getKaryawanId());

        if(keExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, keExist);
        keExist.setDtmUpdate(new Date());
        karyawanRepository.save(keExist);

        KaryawanResponse response = karyawanMapper.map(keExist);

        return response;
    }
}
