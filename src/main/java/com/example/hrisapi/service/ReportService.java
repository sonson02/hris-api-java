package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedReportJamsosResponse;
import com.example.hrisapi.api.base.PaginatedReportKompensasiResponse;
import com.example.hrisapi.api.base.PaginatedReportTagihanGajiResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.response.ReportJamsosResponse;
import com.example.hrisapi.dto.response.ReportKompensasiResponse;
import com.example.hrisapi.dto.response.ReportSPResponse;
import com.example.hrisapi.dto.response.ReportTagihanGajiResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.repository.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private MasterPph21Repository pph21Repository;

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

    public PaginatedReportJamsosResponse<ReportJamsosResponse> getReportJamsos(UUID unitId, String periode, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportJamsosResponse> listReportKaryawan = new ArrayList<>();

        if(unitId!=null){
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if(periode!=null){
            int bulan = HrisConstant.getBulanPeriode(periode);
            int tahun = HrisConstant.getTahunPeriode(periode);

            listKaryawanEntity = karyawanRepository.getKaryawanFilterByPeriode(bulan, tahun);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();
        }

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

        var subTotalA = 0D;
        var managementFee5 = 0D;
        var subTotalAB = 0D;
        var ppn11 = 0D;
        var grandTotal = 0D;
        var terbilang = "";

        for(KaryawanEntity ke : listKaryawanEntity){
            ReportJamsosResponse response = new ReportJamsosResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());
            response.setStatusNikah(ke.getStatusNikah());

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
                totalGajiTambahUangMakan += (int) gajiTambahUangMakan;

                var bpjsKKJkk = gaji * HrisConstant.BPJS_KK_JKK;
                response.setBpjsKkJkk(bpjsKKJkk);
                totalBpjsKKJkk += (int) bpjsKKJkk;

                var bpjsKKJkm = gaji * HrisConstant.BPJS_KK_JKM;
                response.setBpjsKkJkm(bpjsKKJkm);
                totalBpjsKKJkm += (int) bpjsKKJkm;

                var bpjsKKJht = gaji * HrisConstant.BPJS_KK_JHT;
                response.setBpjsKkJht(bpjsKKJht);
                totalBpjsKKJht += (int) bpjsKKJht;

                var bpjsJkkJkmJht = bpjsKKJkk + bpjsKKJkm + bpjsKKJht;
                response.setBpjsKkJkkJkmJht(bpjsJkkJkmJht);
                totalBpjsKKJkkJkmJht += (int) bpjsJkkJkmJht;

                //BPJS KK BEBAN PEGAWAI
                var bpjsKKBebanPegawai = gaji * HrisConstant.BPJS_KK_B_PEG;
                response.setBpjsKkBebanPegawai(bpjsKKBebanPegawai);
                totalBpjsKKBebanPegawai += (int) bpjsKKBebanPegawai;

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
                totalBpjsKSBebanPerusahaan += (int) bpjsKSBebanPerusahaan;

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
                totalBpjsKSBebanPegawai += (int) bpjsKSBebanPegawai;

                //BPJS TK BEBAN PERUSAHAAN
                var bpjsTKBebanPerusahaan = 0D;
                if(gaji<=HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPerusahaan = gaji * HrisConstant.BPJS_KK_B_PEG;
                } else if(gaji > HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPerusahaan = HrisConstant.LIMIT_BPJS_TK * HrisConstant.BPJS_KK_B_PEG;
                }
                response.setBpjsTkBebanPerusahaan(bpjsTKBebanPerusahaan);
                totalBpjsTKBebanPerusahaan += (int) bpjsTKBebanPerusahaan;

                //BPJS TK BEBAN PEGAWAI
                var bpjsTKBebanPegawai = 0D;
                if(gaji<=HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPegawai = gaji * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                } else if(gaji > HrisConstant.LIMIT_BPJS_TK){
                    bpjsTKBebanPegawai = HrisConstant.LIMIT_BPJS_TK * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
                }
                response.setBpjsTkBebanPegawai(bpjsTKBebanPegawai);
                totalBpjsTKBebanPegawai += (int) bpjsTKBebanPegawai;

                //PPH PASAL 21
                String ter = jenisTerForPph21(ke.getStatusNikah());
                MasterPph21Entity masterPph21Ter = pph21Repository.getNominalTer(ter, Double.valueOf(gaji));
                Double nominalTer = masterPph21Ter.getPph21Ter();

                var pph21 = gaji * nominalTer;

                response.setPphPasal21(pph21);
                totalPphPasal21 += (int) pph21;

                //GAJI DITERIMA
                var gajiDiterima = gajiTambahUangMakan - bpjsKKBebanPegawai - bpjsKSBebanPegawai - bpjsTKBebanPegawai;
                response.setGajiDiterima(gajiDiterima);
                totalGajiDiterima += (int) gajiDiterima;
            }
            listReportKaryawan.add(response);
        }
        subTotalA = (int) totalBpjsKKJkkJkmJht + totalBpjsTKBebanPerusahaan + totalBpjsKSBebanPerusahaan + totalPphPasal21;
        managementFee5 = (int) subTotalA * 0.05;
        subTotalAB = (int) subTotalA + managementFee5;
        ppn11 = managementFee5 * 0.11;
        grandTotal = subTotalAB + ppn11;
        terbilang = HrisConstant.angkaToTerbilang(grandTotal);

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
                totalGajiDiterima,
                subTotalA,
                managementFee5,
                subTotalAB,
                ppn11,
                grandTotal,
                terbilang
        );
    }

    public PaginatedReportKompensasiResponse<ReportKompensasiResponse> getReportKompensasi(UUID unitId, String periode, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();
        List<ReportKompensasiResponse> listReportKaryawan = new ArrayList<>();

        if(unitId!=null){
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if(periode!=null){
            int bulan = HrisConstant.getBulanPeriode(periode);
            int tahun = HrisConstant.getTahunPeriode(periode);

            listKaryawanEntity = karyawanRepository.getKaryawanFilterByPeriode(bulan, tahun);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanForReportGaji();
        }

        var totalKompensasiDiterima = 0D;
        var totalManagementFee = 0D;
        var totalTotal = 0D;

        var pph11 = 0D;
        var totalRekap = 0D;
        var terbilang = "";

        for(KaryawanEntity ke : listKaryawanEntity) {
            ReportKompensasiResponse response = new ReportKompensasiResponse();
            response.setKaryawanNip(ke.getKaryawanNip());
            response.setKaryawanName(ke.getKaryawanName());
            response.setTanggalBerlakuKompensasi(HrisConstant.formatDate(new Date()));

            KontrakKerjaEntity kke = kontrakKerjaRepository.getKaryawanNipAndIsActive(ke.getKaryawanNip());

            if (kke != null) {
                JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kke.getJabatanId());
                if (jme != null) {
                    response.setJabatanName(jme.getJabatanName());
                }
            }

            //kompensasi diterima
            var satuanKompensasiDiterima = 0D;
            var kompensasiDiterima = 0D;

            List<KontrakKerjaEntity> listAllKontrakKaryawan = kontrakKerjaRepository.getAllKontrakByKaryawanNip(ke.getKaryawanNip());

            for (KontrakKerjaEntity satuanKaryawanKontrak : listAllKontrakKaryawan) {
                Double monthDiff = kontrakKerjaRepository.getMonthForOnePeriodKontrak(satuanKaryawanKontrak.getKaryawanNip(), satuanKaryawanKontrak.getPeriodKontrak());

                var pembagiBulan = (monthDiff / 12);

                satuanKompensasiDiterima = Math.round(pembagiBulan * satuanKaryawanKontrak.getGaji());
                kompensasiDiterima += (int) satuanKompensasiDiterima;
            }
            response.setKompensasiDiterima(kompensasiDiterima);
            totalKompensasiDiterima += (int) kompensasiDiterima;

            //management fee
            var managementFee = kompensasiDiterima * 0.1;
            response.setManagementFee(managementFee);
            totalManagementFee += (int) managementFee;

            var total = kompensasiDiterima + managementFee;
            response.setTotal(total);
            totalTotal += (int) total;

            listReportKaryawan.add(response);
        }
        pph11 = (int) totalManagementFee * 0.11;
        totalRekap = (int) totalKompensasiDiterima + totalManagementFee + pph11;
        terbilang = HrisConstant.angkaToTerbilang(totalRekap);

        return (PaginatedReportKompensasiResponse<ReportKompensasiResponse>) HrisConstant.extractPaginationListReportKompensasi(
                page,
                size,
                listReportKaryawan,
                totalKompensasiDiterima,
                totalManagementFee,
                totalTotal,
                pph11,
                totalRekap,
                terbilang
        );
    }

    public String jenisTerForPph21(String statusNikah){
        String ter = "";
        switch (statusNikah) {
            case "TK0"   : ter = "A"; break;
            case "TK1"  : ter = "A"; break;
            case "TK2"  : ter = "B"; break;
            case "TK3"  : ter = "B"; break;
            case "K0"   : ter = "A"; break;
            case "K1"   : ter = "B"; break;
            case "K2"   : ter = "B"; break;
            case "K3"   : ter = "C"; break;
        }
        return ter;
    }
}
