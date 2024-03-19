package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedReportResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.ReportSPResponse;
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
public class ReportService {

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    public PaginatedReportResponse<ReportTagihanGajiResponse> getReportTagihanGaji(String nip, String name, UUID unitId, String periode, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportTagihanGajiResponse> listReportKaryawan = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null) {
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if (name!=null) {
            listKaryawanEntity = karyawanRepository.findByKaryawanNameContainingIgnoreCaseAndIsActiveTrue(name);
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
        var totalUangMakan=0D;
        var totalTunjanganKomunikasi=0D;
        var totalTunjanganVariabel=0D;
        var totalTunjanganKhusus=0D;
        var totalPph11=0D;

        for(KaryawanEntity ke : listKaryawanEntity) {
            ReportTagihanGajiResponse response = new ReportTagihanGajiResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());

            KontrakKerjaEntity kke = kontrakKerjaRepository.getKaryawanNipAndIsActive(ke.getKaryawanNip());

            if(kke!=null){
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

                var tunjangan = kke.getTunjangan();
                response.setTunjangan(Double.valueOf(tunjangan));
                totalTunjangan += tunjangan;

                var uangMakan = kke.getUangMakan();
                response.setUangMakan(uangMakan);
                totalUangMakan += uangMakan;

                var tunjanganKomunikasi = kke.getTunjanganKomunikasi();
                response.setTunjanganKomunikasi(tunjanganKomunikasi);
                totalTunjanganKomunikasi += tunjanganKomunikasi;

                var tunjanganVariabel = kke.getTunjanganVariable();
                response.setTunjanganVariable(tunjanganVariabel);
                totalTunjanganVariabel += tunjanganVariabel;

                var tunjanganKhusus = kke.getTunjanganKhusus();
                response.setTunjanganKhusus(tunjanganKhusus);
                totalTunjanganKhusus += tunjanganKhusus;

                var gajiDibayar = gaji + tunjangan + uangMakan + tunjanganKomunikasi + tunjanganVariabel + tunjanganKhusus;
                response.setGajiDibayar(Double.valueOf(gajiDibayar));
                totalGajiDibayar += gajiDibayar;

                var manajemenFee = gajiDibayar * HrisConstant.MANAJEMEN_FEE_PERCENTAGE;
                response.setManajemenFee(manajemenFee);
                totalManajemenFee += manajemenFee;

                var total = gajiDibayar + manajemenFee;
                response.setTotal(total);
                totalTagihanGaji += total;

                totalPph11 = (totalManajemenFee * HrisConstant.MANAJEMEN_FEE_PPH_11);
            }
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
                totalTagihanGaji,
                totalUangMakan,
                totalTunjanganKhusus,
                totalTunjanganVariabel,
                totalTunjanganKomunikasi,
                totalPph11
        );
    }

    public PaginatedResponse<ReportSPResponse> getReportSP(String name, UUID unitId, String periode, Integer page, Integer size) {
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportSPResponse> listReportSPKaryawan = new ArrayList<>();

        if (name != null) {
            listKaryawanEntity = karyawanRepository.getKaryawanSPByName(name);
        } else if (unitId != null) {
            listKaryawanEntity = karyawanRepository.getKaryawanSPByUnitId(unitId);
        } else if (periode != null) {
            int bulan = HrisConstant.getBulanPeriode(periode);
            int tahun = HrisConstant.getTahunPeriode(periode);

            listKaryawanEntity = karyawanRepository.getKaryawanSPFilterByPeriode(bulan, tahun);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanSP();
        }

        for (KaryawanEntity ke : listKaryawanEntity) {
            ReportSPResponse response = new ReportSPResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());

            KontrakKerjaEntity kke = kontrakKerjaRepository.getKaryawanNipAndIsActive(ke.getKaryawanNip());

            if(kke!=null){
                JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kke.getJabatanId());
                if(jme!=null){
                    response.setJabatanName(jme.getJabatanName());
                }

                TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(kke.getTempatTugasId());
                if(ttme!=null){
                    response.setNamaProyek(ttme.getNamaProyek());
                }
            }

            response.setSuratPeringatan(ke.getSuratPeringatan());
            response.setTanggalSuratPeringatan(HrisConstant.formatDate(ke.getTanggalSuratPeringatan()));

            listReportSPKaryawan.add(response);
        }

        return (PaginatedResponse<ReportSPResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listReportSPKaryawan
        );
    }
}
