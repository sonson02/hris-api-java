package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.SlipGajiResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.entity.KaryawanEntity;
import com.example.hrisapi.entity.KontrakKerjaEntity;
import com.example.hrisapi.entity.UnitMasterEntity;
import com.example.hrisapi.repository.JabatanMasterRepository;
import com.example.hrisapi.repository.KaryawanRepository;
import com.example.hrisapi.repository.KontrakKerjaRepository;
import com.example.hrisapi.repository.UnitMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SlipGajiService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    public PaginatedResponse<SlipGajiResponse> getListSlipGaji(String nip, String name, UUID unitId, String periode, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<SlipGajiResponse> listSlipGajiKaryawan = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null) {
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if (name!=null){
            listKaryawanEntity = karyawanRepository.findByKaryawanNameContainingIgnoreCaseAndIsActiveTrue(name);
        } else if(periode!=null){
            int bulan = HrisConstant.getBulanPeriode(periode);
            int tahun = HrisConstant.getTahunPeriode(periode);

            listKaryawanEntity = karyawanRepository.getKaryawanFilterByPeriode(bulan, tahun);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();
        }

        for(KaryawanEntity ke : listKaryawanEntity) {
            SlipGajiResponse response = new SlipGajiResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());

            KontrakKerjaEntity kke = kontrakKerjaRepository.getKaryawanNipAndIsActive(ke.getKaryawanNip());

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kke.getJabatanId());
            if(jme!=null){
                response.setJabatanName(jme.getJabatanName());
            }

            UnitMasterEntity ume = unitMasterRepository.findByUnitId(kke.getUnitId());
            if(ume!=null){
                response.setUnitName(ume.getUnitName());
            }

            response.setPeriode(HrisConstant.formatDateSlipGajiPeriode(kke.getTglMasukKerja()));

            listSlipGajiKaryawan.add(response);
        }

        return (PaginatedResponse<SlipGajiResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listSlipGajiKaryawan
        );
    }
}
