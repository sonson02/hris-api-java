package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedReportResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.ReportTagihanGajiResponse;
import com.example.hrisapi.entity.JabatanMasterEntity;
import com.example.hrisapi.entity.KaryawanEntity;
import com.example.hrisapi.entity.KontrakKerjaEntity;
import com.example.hrisapi.entity.TempatTugasMasterEntity;
import com.example.hrisapi.repository.JabatanMasterRepository;
import com.example.hrisapi.repository.KaryawanRepository;
import com.example.hrisapi.repository.KontrakKerjaRepository;
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
    private KontrakKerjaRepository kontrakKerjaRepository;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    public PaginatedReportResponse<ReportTagihanGajiResponse> getReportTagihanGaji(String nip, UUID unitId, String periode, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportTagihanGajiResponse> listReportKaryawan = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null) {
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if(periode!=null){
            int bulan = HrisConstant.getBulanPeriode(periode);
            int tahun = HrisConstant.getTahunPeriode(periode);

            listKaryawanEntity = karyawanRepository.getKaryawanFilterByPeriode(bulan, tahun);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();
        }

        var totalGaji=0D;
        var totalTunjangan=0D;
        var totalGajiDibayar=0D;
        var totalManajemenFee=0D;
        var totalTagihanGaji=0D;

        for(KaryawanEntity ke : listKaryawanEntity) {
            ReportTagihanGajiResponse response = new ReportTagihanGajiResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());

            KontrakKerjaEntity kke = kontrakKerjaRepository.getKaryawanNipAndIsActive(ke.getKaryawanNip());

            JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kke.getJabatanId());
            if(jme!=null){
                response.setJabatanName(jme.getJabatanName());
            }

            TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(kke.getTempatTugasId());
            if(ttme!=null){
                response.setNamaProyek(ttme.getNamaProyek());
            }

            var gaji = kke.getGaji();
            response.setGaji(gaji);
            totalGaji += gaji;

            var tunjangan = ttme.getNominalTunjangan();
            response.setTunjangan(tunjangan);
            totalTunjangan += tunjangan;

            var gajiDibayar = gaji + tunjangan;
            response.setGajiDibayar(gajiDibayar);
            totalGajiDibayar += gajiDibayar;

            var manajemenFee = gajiDibayar * HrisConstant.MANAJEMEN_FEE_PERCENTAGE;
            response.setManajemenFee(manajemenFee);
            totalManajemenFee += manajemenFee;

            var total = gajiDibayar + manajemenFee;
            response.setTotal(total);
            totalTagihanGaji += total;

            listReportKaryawan.add(response);
        }

        return (PaginatedReportResponse<ReportTagihanGajiResponse>) HrisConstant.extractPaginationListReport(
                page,
                size,
                listReportKaryawan,
                totalGaji,
                totalTunjangan,
                totalGajiDibayar,
                totalManajemenFee,
                totalTagihanGaji
        );
    }
}
