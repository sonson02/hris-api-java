package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.ReportTagihanGajiResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.entity.KaryawanEntity;
import com.example.hrisapi.entity.TempatTugasMasterEntity;
import com.example.hrisapi.repository.JabatanMasterRepository;
import com.example.hrisapi.repository.KaryawanRepository;
import com.example.hrisapi.repository.TempatTugasMasterRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReportTagihanGajiService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    public PaginatedResponse<ReportTagihanGajiResponse> getReportTagihanGaji(String nip, UUID unitId, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null){
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();
        }

        List<ReportTagihanGajiResponse> listReportKaryawan = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity) {
            ReportTagihanGajiResponse response = new ReportTagihanGajiResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(ke.getJabatanId());
            if(jme!=null){
                response.setJabatanName(jme.getJabatanName());
            }

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(ke.getTempatTugasId());
            if(ttme!=null){
                response.setNamaProyek(ttme.getNamaProyek());
            }

            var gaji = ke.getGaji();
            var tunjangan = ttme.getNominalTunjangan();
            response.setGaji(gaji);
            response.setTunjangan(tunjangan);

            var gajiDibayar = gaji + tunjangan;
            response.setGajiDibayar(gajiDibayar);

            var manajemenFee = gajiDibayar * HrisConstant.MANAJEMEN_FEE_PERSENT;
            response.setManajemenFee(manajemenFee);

            var total = gajiDibayar + manajemenFee;
            response.setTotal(total);

            listReportKaryawan.add(response);
        }

        return (PaginatedResponse<ReportTagihanGajiResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listReportKaryawan
        );
    }
}
