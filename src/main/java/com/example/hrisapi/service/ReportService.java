package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedReportJamsosResponse;
import com.example.hrisapi.api.base.PaginatedReportTagihanGajiResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.ReportJamsosResponse;
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

    public PaginatedReportTagihanGajiResponse<ReportTagihanGajiResponse> getReportTagihanGaji(String nip, String name, UUID unitId, String periode, Integer page, Integer size){
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

        return (PaginatedReportTagihanGajiResponse<ReportTagihanGajiResponse>) HrisConstant.extractPaginationListReportTagihanGaji(
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

    public PaginatedReportJamsosResponse<ReportJamsosResponse> getReportJamsos(Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportJamsosResponse> listReportKaryawan = new ArrayList<>();

        listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();

        var totalGaji=0D;
        var totalGajiTambahUangMakan=0D;

        var totalBpjsKKJkk=0D;
        var totalBpjsKKJkm=0D;
        var totalBpjsKKJht=0D;
        var totalBpjsKKJkkJkmJht=0D;

        var totalBpjsKKBebanPegawai=0D;

        var totalBpjsKSBebanPerusahaan=0D;
        var totalBpjsKSBebanPegawai=0D;

        var totalBpjsTKBebanPerusahaan=0D;
        var totalBpjsTKBebanPegawai=0D;

        var totalPphPasal21=0D;
        var totalGajiDiterima=0D;

        for(KaryawanEntity ke : listKaryawanEntity){
            ReportJamsosResponse response = new ReportJamsosResponse();
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

                var gajiTambahUangMakan = gaji + kke.getUangMakan();
                response.setGajiTambahUangMakan(Double.valueOf(gajiTambahUangMakan));
                totalGajiTambahUangMakan += gajiTambahUangMakan;

                var bpjsKKJkk = gaji * HrisConstant.BPJS_KK_JKK;
                response.setBpjsKkJkk(bpjsKKJkk);
                totalBpjsKKJkk += bpjsKKJkk;

                var bpjsKKJkm = gaji * HrisConstant.BPJS_KK_JKM;
                response.setBpjsKkJkm(bpjsKKJkm);
                totalBpjsKKJkm += bpjsKKJkm;

                var bpjsKKJht = gaji * HrisConstant.BPJS_KK_JHT;
                response.setBpjsKkJht(bpjsKKJht);
                totalBpjsKKJht += bpjsKKJht;

                var bpjsJkkJkmJht = bpjsKKJkk + bpjsKKJkm + bpjsKKJht;
                response.setBpjsKkJkkJkmJht(bpjsJkkJkmJht);
                totalBpjsKKJkkJkmJht += bpjsJkkJkmJht;

                //BPJS KK BEBAN PEGAWAI
                var bpjsKKBebanPegawai = gaji * HrisConstant.BPJS_KK_B_PEG;
                response.setBpjsKkBebanPegawai(bpjsKKBebanPegawai);
                totalBpjsKKBebanPegawai += bpjsKKBebanPegawai;

                //BPJS KS BEBAN PERUSAHAAN
                var bpjsKSBebanPerusahaan = 0D;
                if(gaji <= HrisConstant.LIMIT_BAWAH_BPJS_KESEHATAN){
                    bpjsKSBebanPerusahaan = HrisConstant.LIMIT_BAWAH_BPJS_KESEHATAN * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                } else if(gaji <= HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN) {
                    bpjsKSBebanPerusahaan =  gaji * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                } else if(gaji > HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN) {
                    bpjsKSBebanPerusahaan = HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                }
                response.setBpjsKsBebanPerusahaan(bpjsKSBebanPerusahaan);
                totalBpjsKSBebanPerusahaan += bpjsKSBebanPerusahaan;

                //BPJS KS BEBAN PEGAWAI
                var bpjsKSBebanPegawai = 0D;
                if(gaji <= HrisConstant.LIMIT_BAWAH_BPJS_KS_BEBAN_PEGAWAI){
                    bpjsKSBebanPerusahaan = HrisConstant.LIMIT_BAWAH_BPJS_KS_BEBAN_PEGAWAI * HrisConstant.BPJS_KS_BEBAN_PEGAWAI_PERCENTAGE;
                } else if(gaji <= HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN && gaji > HrisConstant.LIMIT_BAWAH_BPJS_KS_BEBAN_PEGAWAI) {
                    bpjsKSBebanPerusahaan =  gaji * HrisConstant.BPJS_KS_BEBAN_PEGAWAI_PERCENTAGE;
                } else if(gaji > HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN) {
                    bpjsKSBebanPerusahaan = HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN * HrisConstant.BPJS_KS_BEBAN_PEGAWAI_PERCENTAGE;
                }
                response.setBpjsKsBebanPegawai(bpjsKSBebanPegawai);
                totalBpjsKSBebanPegawai += bpjsKSBebanPegawai;

                //BPJS TK BEBAN PERUSAHAAN
                var bpjsTKBebanPerusahaan = 0D;
                if(gaji<=HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPerusahaan = gaji * HrisConstant.BPJS_KK_B_PEG;
                } else if(gaji > HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPerusahaan = HrisConstant.LIMIT_BPJS_TK * HrisConstant.BPJS_KK_B_PEG;
                }
                response.setBpjsTkBebanPerusahaan(bpjsTKBebanPerusahaan);
                totalBpjsTKBebanPerusahaan += bpjsTKBebanPerusahaan;

                //BPJS TK BEBAN PEGAWAI
                var bpjsTKBebanPegawai = 0D;
                if(gaji<=HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPegawai = gaji * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                } else if(gaji > HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPegawai = HrisConstant.LIMIT_BPJS_TK * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                }
                response.setBpjsTkBebanPerusahaan(bpjsTKBebanPegawai);
                totalBpjsTKBebanPegawai += bpjsTKBebanPegawai;

                //PPH PASAL 21

                //GAJI DITERIMA
                var gajiDiterima = gajiTambahUangMakan - bpjsKKBebanPegawai - bpjsKSBebanPegawai - bpjsTKBebanPegawai;
                response.setGajiDiterima(gajiDiterima);
                totalGajiDiterima += gajiDiterima;
            }
            listReportKaryawan.add(response);
        }

        return (PaginatedReportJamsosResponse<ReportJamsosResponse>) HrisConstant.extractPaginationListReportJamsos(
                page,
                size,
                listReportKaryawan,
                totalGaji,
                totalGajiTambahUangMakan,
                totalBpjsKKJkk,
                totalBpjsKKJkm,
                totalBpjsKKJht,
                totalBpjsKKJkkJkmJht,
                totalBpjsKKBebanPegawai,
                totalBpjsKSBebanPerusahaan,
                totalBpjsKSBebanPegawai,
                totalBpjsTKBebanPerusahaan,
                totalBpjsTKBebanPegawai,
                totalPphPasal21,
                totalGajiDiterima
        );
    }
}
