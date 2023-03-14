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

            KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());
            response.setTglMasukKerja(ke.getTglMasukKerja());
            response.setTglHabisKontrak(ke.getTglHabisKontrak());

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(ke.getTempatTugasId());
            if(ttme!=null){
                response.setNamaProyek(ttme.getNamaProyek());
            }

            UnitMasterEntity ume = unitMasterRepository.findByUnitId(ke.getUnitId());
            if(ume!=null){
                response.setUnitName(ume.getUnitName());
            }

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(ke.getJabatanId());
            if(jme!=null){
                response.setJabatanName(jme.getJabatanName());
            }

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
        kke.setDtmUpdate(new Date());
        kke.setIsActive(true);
        kontrakKerjaRepository.save(kke);

        KaryawanEntity k = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
        k.setGaji(request.getGaji());
        k.setUangTelekomunikasi(request.getUangTelekomunikasi());
        k.setTglMasukKerja(request.getTglMasukKerja());
        k.setTglHabisKontrak(request.getTglHabisKontrak());
        k.setTipeTunjangan(request.getTipeTunjangan());
        karyawanRepository.save(k);

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kke);
        response.setKaryawanName(k.getKaryawanName());

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

        KaryawanEntity k = karyawanRepository.findByKaryawanNip(kkeExist.getKaryawanNip());
        k.setGaji(request.getGaji());
        k.setUangTelekomunikasi(request.getUangTelekomunikasi());
        k.setTglMasukKerja(request.getTglMasukKerja());
        k.setTglHabisKontrak(request.getTglHabisKontrak());
        k.setTipeTunjangan(request.getTipeTunjangan());
        karyawanRepository.save(k);

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
