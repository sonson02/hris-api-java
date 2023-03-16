package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.response.KaryawanByNipResponse;
import com.example.hrisapi.dto.response.KaryawanResponse;
import com.example.hrisapi.dto.response.ListKaryawanResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.mapper.KaryawanMapper;
import com.example.hrisapi.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
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

    @Autowired
    private FileUploadRepository fileUploadRepository;

    public PaginatedResponse<ListKaryawanResponse> getListKaryawan(String nip, UUID unitId, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null){
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanIsActive();
        }

        List<ListKaryawanResponse> listKaryawanResponse = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity){
            ListKaryawanResponse response = karyawanMapper.mapList(ke);

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

            response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));

            List<KontrakKerjaEntity> listKkExistCekPeriod = kontrakKerjaRepository.findByKaryawanNip(ke.getKaryawanNip());
            for(KontrakKerjaEntity kke : listKkExistCekPeriod){
                response.setPeriodKontrak(kontrakKerjaRepository.getMaxPeriodKontrakForListKaryawan(ke.getKaryawanNip()));
            }

            listKaryawanResponse.add(response);
        }

        return (PaginatedResponse<ListKaryawanResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKaryawanResponse
        );
    }

    @Transactional
    public KaryawanResponse insertKaryawan(KaryawanRequest request) throws ParseException {

        FileUploadEntity fileCv = new FileUploadEntity();
        fileCv.setFileUploadId(UUID.randomUUID());
        fileCv.setFileName(request.getLampiranCv());
        fileUploadRepository.save(fileCv);

        KaryawanEntity ke = karyawanMapper.mapRequest(request);
        ke.setKaryawanId(UUID.randomUUID());
        ke.setIsActive(true);
        ke.setFileUploadId(fileCv.getFileUploadId());
        ke.setGaji(0D);
        ke.setUangTelekomunikasi(0D);
        ke.setDtmUpdate(new Date());
        karyawanRepository.save(ke);

        KaryawanResponse response = karyawanMapper.map(ke);
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
        response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));

        return response;
    }

    @Transactional
    public KaryawanResponse updateKaryawan(KaryawanRequest request){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanId(request.getKaryawanId());

        if(keExist==null){
            throw new DataNotFoundException();
        }

        FileUploadEntity fileCv = fileUploadRepository.findByFileUploadId(keExist.getFileUploadId());
        fileCv.setFileName(request.getLampiranCv());
        fileUploadRepository.save(fileCv);

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, keExist);
        keExist.setDtmUpdate(new Date());
        karyawanRepository.save(keExist);

        KaryawanResponse response = karyawanMapper.map(keExist);
        response.setTanggalLahir(HrisConstant.formatDate(keExist.getTanggalLahir()));
        response.setTglHabisKontrak(HrisConstant.formatDate(keExist.getTglHabisKontrak()));
        response.setTglMasukKerja(HrisConstant.formatDate(keExist.getTglMasukKerja()));

        return response;
    }

    public KaryawanResponse getDetailKaryawan(UUID karyawanId){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanId(karyawanId);

        if(keExist==null){
            throw new DataNotFoundException();
        }

        KaryawanResponse response = karyawanMapper.map(keExist);
        response.setTanggalLahir(HrisConstant.formatDate(keExist.getTanggalLahir()));
        response.setTglHabisKontrak(HrisConstant.formatDate(keExist.getTglHabisKontrak()));
        response.setTglMasukKerja(HrisConstant.formatDate(keExist.getTglMasukKerja()));

        return response;
    }

    public KaryawanByNipResponse getKaryawanByNip(String karyawanNip){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanNip(karyawanNip);

        if(keExist==null){
            throw new DataNotFoundException();
        }

        KaryawanByNipResponse response = karyawanMapper.mapNip(keExist);
        response.setTanggalLahir(HrisConstant.formatDate(keExist.getTanggalLahir()));

        return response;
    }
}
