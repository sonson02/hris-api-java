package com.example.hrisapi.service;

import com.example.hrisapi.dto.response.IuranMasterResponse;
import com.example.hrisapi.dto.response.KontrakKerjaTglHabisKontrakResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.repository.JabatanMasterRepository;
import com.example.hrisapi.repository.UnitMasterRepository;
import com.example.hrisapi.repository.KaryawanRepository;
import com.example.hrisapi.repository.KontrakKerjaRepository;
import com.example.hrisapi.repository.TempatTugasMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    public PaginatedResponse<KontrakKerjaTglHabisKontrakResponse> schedulerTglHabisKontrak(Integer page, Integer size){
        List<KontrakKerjaTglHabisKontrakResponse> listTglHbsKontrakResponse = new ArrayList<>();

        List<KontrakKerjaEntity> listKaryawanTglHbisKontrak = kontrakKerjaRepository.getKaryawanTglHabisKontrak();

        for(KontrakKerjaEntity kke : listKaryawanTglHbisKontrak){
            kke.setIsActive(false);
            kontrakKerjaRepository.save(kke);

            KontrakKerjaTglHabisKontrakResponse response = new KontrakKerjaTglHabisKontrakResponse();
            response.setKontrakId(kke.getKontrakId());
            response.setKaryawanNip(kke.getKaryawanNip());

            KaryawanEntity ke = karyawanRepository.findByKaryawanNip(kke.getKaryawanNip());
            if(ke!=null){
                response.setKaryawanName(ke.getKaryawanName());
            }

            UnitMasterEntity ume = unitMasterRepository.findByUnitId(kke.getUnitId());
            if(ume!=null){
                response.setUnitName(ume.getUnitName());
            }

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kke.getJabatanId());
            if(jme!=null){
                response.setJabatanName(jme.getJabatanName());
            }

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(kke.getTempatTugasId());
            if(ttme!=null){
                response.setNamaProyek(ttme.getNamaProyek());
            }

            response.setTglHabisKontrak(HrisConstant.formatDate(kke.getTglHabisKontrak()));
            response.setPeriodKontrak(kke.getPeriodKontrak());
            response.setIsActive(kke.getIsActive());

            listTglHbsKontrakResponse.add(response);
        }

        return (PaginatedResponse<KontrakKerjaTglHabisKontrakResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listTglHbsKontrakResponse
        );
    }
}
