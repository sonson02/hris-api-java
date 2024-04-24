package com.example.hrisapi.service;

import com.example.hrisapi.dto.response.HistoryGajiResponse;
import com.example.hrisapi.dto.response.KontrakKerjaTglHabisKontrakResponse;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.repository.*;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private HistoryGajiRepository historyGajiRepository;

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

    public PaginatedResponse<HistoryGajiResponse> schedulerHistoryGaji(Integer page, Integer size){
        List<HistoryGajiResponse> listHistoryGajiResponse = new ArrayList<>();

        LocalDate today = LocalDate.now();
        int bulanNow = today.getMonthValue();
        int tahunNow = today.getYear();

        List<KontrakKerjaEntity> listKontrakKerja = kontrakKerjaRepository.getKontrakKerjaIsActive();
        for(KontrakKerjaEntity kke : listKontrakKerja){

            List<HistoryGajiEntity> listHgeByNip = historyGajiRepository.getKaryawanNipHistoryGaji(kke.getKaryawanNip());

            if(!listHgeByNip.isEmpty()){
                for(HistoryGajiEntity hgeByNip : listHgeByNip) {
                    var bulanExist = hgeByNip.getBulan();
                    var tahunExist = hgeByNip.getTahun();

                    if(hgeByNip!=null && bulanExist==bulanNow && tahunExist==tahunNow){
                        HistoryGajiEntity hgExist = updateToHistoryGaji(hgeByNip, kke);
                        historyGajiRepository.save(hgExist);

                        HistoryGajiResponse response = responseHistoryGaji(hgExist);
                        listHistoryGajiResponse.add(response);
                    } else {
                        HistoryGajiEntity hg = insertToHistoryGaji(kke);
                        historyGajiRepository.save(hg);

                        HistoryGajiResponse response = responseHistoryGaji(hg);
                        listHistoryGajiResponse.add(response);
                    }
                }
            } else {
                HistoryGajiEntity hg = insertToHistoryGaji(kke);
                historyGajiRepository.save(hg);

                HistoryGajiResponse response = responseHistoryGaji(hg);
                listHistoryGajiResponse.add(response);
            }
        }

        return (PaginatedResponse<HistoryGajiResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listHistoryGajiResponse
        );
    }

    private HistoryGajiEntity insertToHistoryGaji(KontrakKerjaEntity kke){

        LocalDate today = LocalDate.now();
        int bulanNow = today.getMonthValue();
        int tahunNow = today.getYear();

        HistoryGajiEntity hge = new HistoryGajiEntity();
        hge.setHistoryGajiId(UUID.randomUUID());
        hge.setKaryawanNip(kke.getKaryawanNip());
        hge.setBulan(bulanNow);
        hge.setTahun(tahunNow);
        hge.setGaji(Double.valueOf(kke.getGaji()));
        hge.setTunjanganKomunikasi(Double.valueOf(kke.getTunjanganKomunikasi()));
        hge.setUangMakan(Double.valueOf(kke.getUangMakan()));
        hge.setTunjangan(Double.valueOf(kke.getTunjangan()));
        hge.setTunjanganKhusus(Double.valueOf(kke.getTunjanganKhusus()));
        hge.setTunjanganVariable(Double.valueOf(kke.getTunjanganVariable()));
        hge.setDtmUpdate(kke.getDtmUpdate());
        hge.setUsrUpdate(kke.getUsrUpdate());

        return hge;
    }

    private HistoryGajiEntity updateToHistoryGaji(HistoryGajiEntity hgeExist, KontrakKerjaEntity kke){
        hgeExist.setGaji(Double.valueOf(kke.getGaji()));
        hgeExist.setTunjanganKomunikasi(Double.valueOf(kke.getTunjanganKomunikasi()));
        hgeExist.setUangMakan(Double.valueOf(kke.getUangMakan()));
        hgeExist.setTunjangan(Double.valueOf(kke.getTunjangan()));
        hgeExist.setTunjanganKhusus(Double.valueOf(kke.getTunjanganKhusus()));
        hgeExist.setTunjanganVariable(Double.valueOf(kke.getTunjanganVariable()));
        hgeExist.setDtmUpdate(kke.getDtmUpdate());
        hgeExist.setUsrUpdate(kke.getUsrUpdate());

        return hgeExist;
    }

    private HistoryGajiResponse responseHistoryGaji(HistoryGajiEntity hge){
        HistoryGajiResponse response = new HistoryGajiResponse();

        response.setHistoryGajiId(hge.getHistoryGajiId());
        response.setKaryawanNip(hge.getKaryawanNip());
        response.setBulan(hge.getBulan());
        response.setTahun(hge.getTahun());
        response.setGaji(hge.getGaji());
        response.setTunjanganKomunikasi(hge.getTunjanganKomunikasi());
        response.setUangMakan(hge.getUangMakan());
        response.setTunjangan(hge.getTunjangan());
        response.setTunjanganKhusus(hge.getTunjanganKhusus());
        response.setTunjanganVariable(hge.getTunjanganVariable());
        response.setDtmUpdate(HrisConstant.formatDate(hge.getDtmUpdate()));
        response.setUsrUpdate(hge.getUsrUpdate());

        return response;
    }
}
