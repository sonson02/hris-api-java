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

    public PaginatedResponse<KontrakKerjaResponse> getListKontrak(String nip, UUID unitId, Integer page, Integer size){
        List<KontrakKerjaEntity> listKontrakKerjaEntity = new ArrayList<>();

        if(nip!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.findByKaryawanNip(nip);
        } else if (unitId!=null){
            listKontrakKerjaEntity = kontrakKerjaRepository.getFilterKontrakByUnitIdAndIsActive(unitId);
        } else {
            listKontrakKerjaEntity = kontrakKerjaRepository.getKontrakKerjaIsActive();
        }

        List<KontrakKerjaResponse> listKontrakKerjaResponse = new ArrayList<>();

        for(KontrakKerjaEntity kke : listKontrakKerjaEntity){
            KontrakKerjaResponse response = kontrakKerjaMapper.map(kke);

            KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());
            response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));
            response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
            response.setNonik(ke.getNonik());
            response.setGaji(ke.getGaji());

            response.setUnitId(ke.getUnitId());
            response.setTempatTugasId(ke.getTempatTugasId());
            response.setJabatanId(ke.getJabatanId());
            response.setTipeTunjangan(ke.getTipeTunjangan());

            response.setTempatTinggal(ke.getTempatTinggal());
            response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));

            response.setRequestDate(HrisConstant.formatDate(kke.getRequestDate()));
            response.setRequestNo(kke.getRequestNo());
            response.setUangMakan(ke.getUangMakan());

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

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
        ke.setGaji(request.getGaji());
        ke.setTglMasukKerja(request.getTglMasukKerja());
        ke.setTglHabisKontrak(request.getTglHabisKontrak());
        ke.setTipeTunjangan(request.getTipeTunjangan());

        ke.setTempatTugasId(request.getTempatTugasId());
        ke.setUnitId(request.getUnitId());
        ke.setJabatanId(request.getJabatanId());
        ke.setUangMakan(request.getUangMakan());
        karyawanRepository.save(ke);

        kke.setKaryawanId(ke.getKaryawanId());
        kke.setPeriodKontrak(kontrakKerjaRepository.getCountPeriodKontrak(ke.getKaryawanNip()));
        kontrakKerjaRepository.save(kke);

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kke);
        response.setKaryawanName(ke.getKaryawanName());
        response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
        response.setNonik(ke.getNonik());
        response.setGaji(ke.getGaji());

        response.setUnitId(request.getUnitId());
        response.setTempatTugasId(request.getTempatTugasId());
        response.setJabatanId(request.getJabatanId());

        response.setTempatTinggal(ke.getTempatTinggal());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));

        response.setTipeTunjangan(request.getTipeTunjangan());

        response.setRequestNo(request.getRequestNo());
        response.setRequestDate(HrisConstant.formatDate(kke.getRequestDate()));
        response.setUangMakan(request.getUangMakan());

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

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kkeExist.getKaryawanNip());
        ke.setGaji(request.getGaji());
        ke.setTglMasukKerja(request.getTglMasukKerja());
        ke.setTglHabisKontrak(request.getTglHabisKontrak());
        ke.setTipeTunjangan(request.getTipeTunjangan());

        ke.setTempatTugasId(request.getTempatTugasId());
        ke.setUnitId(request.getUnitId());
        ke.setJabatanId(request.getJabatanId());

        ke.setUangMakan(request.getUangMakan());
        karyawanRepository.save(ke);

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kkeExist);
        response.setKaryawanName(ke.getKaryawanName());
        response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
        response.setNonik(ke.getNonik());
        response.setGaji(ke.getGaji());

        response.setUnitId(request.getUnitId());
        response.setTempatTugasId(request.getTempatTugasId());
        response.setJabatanId(request.getJabatanId());

        response.setTempatTinggal(ke.getTempatTinggal());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));

        response.setTipeTunjangan(request.getTipeTunjangan());

        response.setRequestNo(request.getRequestNo());
        response.setRequestDate(HrisConstant.formatDate(request.getRequestDate()));
        response.setUangMakan(request.getUangMakan());

        return response;
    }

    public KontrakKerjaResponse getDetailKontrak(UUID kontrakId){

        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.findByKontrakId(kontrakId);

        if(kkeExist==null){
            throw new DataNotFoundException();
        }

        KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kkeExist.getKaryawanNip());

        KontrakKerjaResponse response = kontrakKerjaMapper.map(kkeExist);
        response.setKaryawanName(ke.getKaryawanName());
        response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
        response.setNonik(ke.getNonik());
        response.setGaji(ke.getGaji());
        response.setKontrakKode(kkeExist.getKontrakKode());

        response.setUnitId(ke.getUnitId());
        response.setTempatTugasId(ke.getTempatTugasId());
        response.setJabatanId(ke.getJabatanId());
        response.setTipeTunjangan(ke.getTipeTunjangan());

        response.setTempatTinggal(ke.getTempatTinggal());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));

        response.setRequestDate(HrisConstant.formatDate(kkeExist.getRequestDate()));
        response.setUangMakan(ke.getUangMakan());

        return response;
    }
}
