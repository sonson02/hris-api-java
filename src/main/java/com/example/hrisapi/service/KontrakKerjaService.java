package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KontrakKerjaRequest;
import com.example.hrisapi.dto.response.KontrakKerjaResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.mapper.KontrakKerjaMapper;
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
public class KontrakKerjaService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private KontrakKerjaMapper kontrakKerjaMapper;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    public PaginatedResponse<KontrakKerjaResponse> getListKontrak(String nip, String unitName, Integer page, Integer size){
        List<KontrakKerjaEntity> listKontrakKerjaEntity = kontrakKerjaRepository.getKontrakKerjaIsActive();
        List<KontrakKerjaResponse> listKontrakKerjaResponse = new ArrayList<>();

        for(KontrakKerjaEntity kke : listKontrakKerjaEntity){
            KontrakKerjaResponse response = kontrakKerjaMapper.map(kke);

            KaryawanEntity k = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
            response.setKaryawanName(k.getKaryawanName());
            response.setTglMasukKerja(k.getTglMasukKerja());
            response.setTglHabisKontrak(k.getTglHabisKontrak());

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(kke.getTempatTugasId());
            response.setNamaProyek(ttme.getNamaProyek());

            UnitMasterEntity ume = unitMasterRepository.findByUnitId(k.getUnitId());
            response.setUnitName(ume.getUnitName());

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(k.getJabatanId());
            response.setJabatanName(jme.getJabatanName());

            listKontrakKerjaResponse.add(response);
        }

        return (PaginatedResponse<KontrakKerjaResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKontrakKerjaResponse
        );
    }

    @Transactional
    public KontrakKerjaResponse insertKontrak(KontrakKerjaRequest request){
        KontrakKerjaEntity kke = kontrakKerjaMapper.mapRequest(request);
        kke.setKontrakId(UUID.randomUUID());
        kke.setIsActive(true);
        kontrakKerjaRepository.save(kke);

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kke);

        return response;
    }

    @Transactional
    public KontrakKerjaResponse updateKontrak(KontrakKerjaRequest request){

        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.findByKontrakId(request.getKontrakId());

        if(kkeExist==null){
            throw new DataNotFoundException();
        }

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, kkeExist);
        kkeExist.setDtmUpdate(new Date());
        kontrakKerjaRepository.save(kkeExist);

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kkeExist);

        return response;
    }

    public KontrakKerjaResponse getDetailKontrak(UUID kontrakId){

        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.findByKontrakId(kontrakId);

        if(kkeExist==null){
            throw new DataNotFoundException();
        }

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kkeExist);

        return response;
    }
}
