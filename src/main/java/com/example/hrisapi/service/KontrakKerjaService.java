package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.ContractAlreadyExistException;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.api.exception.RequestNumberAlreadyExistException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KontrakKerjaRequest;
import com.example.hrisapi.dto.request.StopKontrakRequest;
import com.example.hrisapi.dto.response.KontrakKerjaByNipResponse;
import com.example.hrisapi.dto.response.KontrakKerjaResponse;
import com.example.hrisapi.dto.response.StopKontrakResponse;
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

    public PaginatedResponse<KontrakKerjaResponse> getListKontrak(String nip, String name, UUID unitId, Integer page, Integer size){
        List<KontrakKerjaEntity> listKontrakKerjaEntity = new ArrayList<>();

        if(nip!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.findByKaryawanNipAndIsActiveTrue(nip);
        } else if (unitId!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.getFilterKontrakByUnitIdAndIsActive(unitId);
        } else if (name!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.getFilterKontrakByKaryawanNameAndIsActive(name);
        }else {
            listKontrakKerjaEntity = kontrakKerjaRepository.getKontrakKerjaIsActive();
        }

        List<KontrakKerjaResponse> listKontrakKerjaResponse = new ArrayList<>();

        for(KontrakKerjaEntity kke : listKontrakKerjaEntity){

            KontrakKerjaResponse response = new KontrakKerjaResponse();

            KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());

            if(ke.getIsActive()==true){
                extractedEntityToResponse(kke, response, ke);

                listKontrakKerjaResponse.add(response);
            }
        }

        return (PaginatedResponse<KontrakKerjaResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKontrakKerjaResponse
        );
    }

    private void extractedEntityToResponse(KontrakKerjaEntity kke, KontrakKerjaResponse response, KaryawanEntity ke) {
        response.setKontrakId(kke.getKontrakId());
        response.setKaryawanNip(kke.getKaryawanNip());
        response.setKaryawanName(ke.getKaryawanName());
        response.setNonik(ke.getNonik());
        response.setTempatLahir(ke.getTempatLahir());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));
        response.setKontrakKode(kke.getKontrakKode());
        response.setRequestNo(kke.getRequestNo());
        response.setGaji(kke.getGaji());
        response.setTglMasukKerja(HrisConstant.formatDate(kke.getTglMasukKerja()));
        response.setTglHabisKontrak(HrisConstant.formatDate(kke.getTglHabisKontrak()));
        response.setTempatTugasId(kke.getTempatTugasId());
        response.setUnitId(kke.getUnitId());
        response.setJabatanId(kke.getJabatanId());
        response.setIsActive(kke.getIsActive());
        response.setPeriodKontrak(String.valueOf(kontrakKerjaRepository.getCountPeriodKontrak(ke.getKaryawanNip())));
        response.setRequestDate(HrisConstant.formatDate(kke.getRequestDate()));
        response.setTunjanganKomunikasi(kke.getTunjanganKomunikasi());
        response.setUangMakan(kke.getUangMakan());
        response.setTunjangan(kke.getTunjangan());
        response.setTunjanganKhusus(kke.getTunjanganKhusus());
        response.setTunjanganVariable(kke.getTunjanganVariable());
        response.setUsrUpdate(kke.getUsrUpdate());
    }

    @Transactional
    public KontrakKerjaResponse insertKontrak(KontrakKerjaRequest request){

        List<KontrakKerjaEntity> listKontrak = kontrakKerjaRepository.findAll();
        for(KontrakKerjaEntity k : listKontrak){
            if(k.getRequestNo().equalsIgnoreCase(request.getRequestNo())){
                throw new RequestNumberAlreadyExistException();
            }

            if(k.getKontrakKode().equalsIgnoreCase(request.getKontrakKode())){
                throw new ContractAlreadyExistException();
            }
        }

        KontrakKerjaEntity kke = new KontrakKerjaEntity();
        extractedRequestToEntity(request, kke);

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());

        if(ke.getIsActive()==true){
            kke.setKaryawanId(ke.getKaryawanId());
            kke.setPeriodKontrak(kontrakKerjaRepository.getCountPeriodKontrak(ke.getKaryawanNip()));
            kontrakKerjaRepository.save(kke);

            KontrakKerjaResponse response = new KontrakKerjaResponse();
            extractedEntityToResponse(kke, response, ke);
            return response;
        }

        return null;
    }

    private void extractedRequestToEntity(KontrakKerjaRequest request, KontrakKerjaEntity kke) {
        kke.setKontrakId(UUID.randomUUID());
        kke.setIsActive(true);
        kke.setKaryawanNip(request.getKaryawanNip());
        kke.setKontrakKode(request.getKontrakKode());
        kke.setRequestNo(request.getRequestNo());
        kke.setGaji(request.getGaji());
        kke.setTglMasukKerja(request.getTglMasukKerja());
        kke.setTglHabisKontrak(request.getTglHabisKontrak());
        kke.setTempatTugasId(request.getTempatTugasId());
        kke.setUnitId(request.getUnitId());
        kke.setJabatanId(request.getJabatanId());
        kke.setRequestDate(request.getRequestDate());
        kke.setTunjanganKomunikasi(request.getTunjanganKomunikasi());
        kke.setUangMakan(request.getUangMakan());
        kke.setTunjangan(request.getTunjangan());
        kke.setTunjanganKhusus(request.getTunjanganKhusus());
        kke.setTunjanganVariable(request.getTunjanganVariable());
        kontrakKerjaRepository.save(kke);
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

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kkeExist.getKaryawanNip());

        if(ke.getIsActive()){
            KontrakKerjaResponse response = new KontrakKerjaResponse();
            extractedEntityToResponse(kkeExist,response,ke);

            return response;
        }
        return null;
    }

    public KontrakKerjaResponse getDetailKontrak(UUID kontrakId){

        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.findByKontrakId(kontrakId);

        if(kkeExist==null){
            throw new DataNotFoundException();
        }

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kkeExist.getKaryawanNip());

        if(ke.getIsActive()==true){
            KontrakKerjaResponse response = new KontrakKerjaResponse();
            extractedEntityToResponse(kkeExist, response, ke);
            response.setRequestDate(HrisConstant.formatDate(kkeExist.getRequestDate()));

            return response;
        }
        return null;
    }

    public PaginatedResponse<KontrakKerjaByNipResponse> getListKontrakByNip(String nip, Integer page, Integer size){
        List<KontrakKerjaEntity> listKontrakKerjaEntity = new ArrayList<>();

        if(nip!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.findByKaryawanNip(nip);
        }

        List<KontrakKerjaByNipResponse> listKontrakKerjaResponse = new ArrayList<>();

        for(KontrakKerjaEntity kke : listKontrakKerjaEntity){
            KontrakKerjaByNipResponse response = kontrakKerjaMapper.mapByNip(kke);

            KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());

            response.setKaryawanName(ke.getKaryawanName());
            response.setTglHabisKontrak(HrisConstant.formatDate(kke.getTglHabisKontrak()));
            response.setUnitId(kke.getUnitId());
            listKontrakKerjaResponse.add(response);
        }

        return (PaginatedResponse<KontrakKerjaByNipResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKontrakKerjaResponse
        );
    }

    public StopKontrakResponse stopKontrak(StopKontrakRequest request){

        StopKontrakResponse response = new StopKontrakResponse();
        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.findByKontrakId(request.getKontrakId());

        if(kkeExist==null){
            throw new DataNotFoundException();
        }

        kkeExist.setIsActive(false);
        kontrakKerjaRepository.save(kkeExist);

        response.setKontrakId(request.getKontrakId());
        response.setAlasan(request.getAlasan());

        return response;
    }
}
