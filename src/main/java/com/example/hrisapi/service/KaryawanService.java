package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedDashboardResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.api.exception.NikAlreadyExistException;
import com.example.hrisapi.api.exception.NipAlreadyExistException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.request.StopKaryawanRequest;
import com.example.hrisapi.dto.response.*;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;

@Service
public class KaryawanService {

    @Autowired
    private KaryawanRepository karyawanRepository;

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

    @Autowired
    private BankMasterRepository bankMasterRepository;

    @Autowired
    private KaryawanHistoryRepository karyawanHistoryRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public PaginatedResponse<ListKaryawanResponse> getListKaryawan(String nip, String name, UUID unitId, Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();

        if(nip!=null){
            KaryawanEntity karyawanFilterByNip = karyawanRepository.getFilterKaryawanNipAndIsActive(nip);
            listKaryawanEntity.add(karyawanFilterByNip);
        } else if (unitId!=null) {
            listKaryawanEntity = karyawanRepository.getFilterKaryawanByUnitIdAndIsActive(unitId);
        } else if (name!=null){
            listKaryawanEntity = karyawanRepository.findByKaryawanNameContainingIgnoreCaseAndIsActiveTrue(name);
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanIsActive();
        }

        List<ListKaryawanResponse> listKaryawanResponse = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity){
            extracted(listKaryawanResponse, ke);
        }

        return (PaginatedResponse<ListKaryawanResponse>) HrisConstant.extractPaginationList(
                page,
                size,
                listKaryawanResponse
        );
    }

    @Transactional
    public KaryawanResponse insertKaryawan(KaryawanRequest request) throws ParseException {
        List<KaryawanEntity> listKaryawanAll = karyawanRepository.findAll();
        for(KaryawanEntity k : listKaryawanAll){
            if(k.getIsActive()==true){
                if(k.getKaryawanNip().equalsIgnoreCase(request.getKaryawanNip())){
                    throw new NipAlreadyExistException();
                }

                if(k.getNonik().equalsIgnoreCase(request.getNonik())){
                    throw new NikAlreadyExistException();
                }
            }
        }
        KaryawanEntity ke = getKaryawanEntity(request);

        KaryawanResponse response = getKaryawanResponse(ke);
        return response;
    }

    @Transactional
    public KaryawanResponse updateKaryawan(KaryawanRequest request){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanId(request.getKaryawanId());

        if(keExist==null){
            throw new DataNotFoundException();
        }

        KaryawanHistoryEntity keH = getKaryawanHistoryEntity(keExist);
        karyawanHistoryRepository.save(keH);

        FileUploadEntity fileCv = fileUploadRepository.findByFileUploadId(keExist.getFileUploadId());
        fileCv.setFileName(request.getLampiranCv());
        fileUploadRepository.save(fileCv);

        ModelMapper objmapper = new ModelMapper();
        objmapper.getConfiguration().setSkipNullEnabled(true);
        objmapper.map(request, keExist);
        keExist.setDtmUpdate(new Date());

        try {
            keExist.setPendidikanTerakhir(objectMapper.writeValueAsString(request.getPendidikanTerakhir()));
            keExist.setRiwayatPekerjaan(objectMapper.writeValueAsString(request.getRiwayatPekerjaan()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        keExist.setSuratPeringatan(request.getSuratPeringatan());
        keExist.setTanggalSuratPeringatan(request.getTanggalSuratPeringatan());

        karyawanRepository.save(keExist);

        KaryawanResponse response = getKaryawanResponse(keExist);

        return response;
    }

    public KaryawanResponse getDetailKaryawan(UUID karyawanId){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanId(karyawanId);

        if(keExist==null){
            throw new DataNotFoundException();
        }

        KaryawanResponse response = getKaryawanResponse(keExist);

        return response;
    }

    public KaryawanByNipResponse getKaryawanByNip(String karyawanNip){

        KaryawanEntity keExist = karyawanRepository.findByKaryawanNip(karyawanNip);

        if(keExist==null){
            throw new DataNotFoundException();
        }

        KaryawanByNipResponse response = getKaryawanByNipResponse(keExist);

        return response;
    }

    public PaginatedDashboardResponse<ListKaryawanResponse> getKaryawanDashboard(Integer page, Integer size, Integer days){

        List<KaryawanEntity> listKaryawanEntity = new ArrayList<>();

        if(days!=null) {
            switch (days) {
                case 30 :
                    listKaryawanEntity = karyawanRepository.getKaryawanDashboard30Days();
                    break;

                case 60 :
                    listKaryawanEntity = karyawanRepository.getKaryawanDashboard60Days();
                    break;

                case 90 :
                    listKaryawanEntity = karyawanRepository.getKaryawanDashboard90Days();
                    break;

                default :
                    throw new DataNotFoundException();
            }
        } else {
            listKaryawanEntity = karyawanRepository.getKaryawanDashboardAll();
        }

        List<ListKaryawanResponse> listKaryawanResponse = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity){
            extracted(listKaryawanResponse, ke);
        }

        var count30Days = karyawanRepository.getTotalKaryawanDashboard_30_Days();
        var count60Days = karyawanRepository.getTotalKaryawanDashboard_60_Days();
        var count90Days = karyawanRepository.getTotalKaryawanDashboard_90_Days();

        var totalKaryawanActive = karyawanRepository.getTotalKaryawanActive();
        var totalKaryawanBaru = karyawanRepository.getTotalKaryawanBaru();
        var totalKaryawanBerhenti = karyawanRepository.getTotalKaryawanBerhenti();

        return (PaginatedDashboardResponse<ListKaryawanResponse>) HrisConstant.extractPaginationDashboard(
                page,
                size,
                listKaryawanResponse,
                count30Days,
                count60Days,
                count90Days,
                totalKaryawanActive,
                totalKaryawanBaru,
                totalKaryawanBerhenti
        );
    }

    public StopKaryawanResponse stopKaryawan(StopKaryawanRequest request){

        StopKaryawanResponse response = new StopKaryawanResponse();
        KaryawanEntity kExist = karyawanRepository.findByKaryawanId(request.getKaryawanId());

        if(kExist==null){
            throw new DataNotFoundException();
        }

        kExist.setIsActive(false);
        karyawanRepository.save(kExist);

        response.setKaryawanId(request.getKaryawanId());

        return response;
    }

    private KaryawanEntity getKaryawanEntity(KaryawanRequest request) {
        FileUploadEntity fileCv = new FileUploadEntity();
        fileCv.setFileUploadId(UUID.randomUUID());
        fileCv.setFileName(request.getLampiranCv());
        fileUploadRepository.save(fileCv);

        KaryawanEntity ke = new KaryawanEntity();
        ke.setKaryawanId(UUID.randomUUID());
        ke.setKaryawanNip(request.getKaryawanNip());
        ke.setKaryawanName(request.getKaryawanName());
        ke.setTempatLahir(request.getTempatLahir());
        ke.setTanggalLahir(request.getTanggalLahir());
        ke.setAgama(request.getAgama());
        ke.setGender(request.getGender());
        ke.setStatusNikah(request.getStatusNikah());
        ke.setAlamatRumah(request.getAlamatRumah());
        ke.setBankId(request.getBankId());
        ke.setNoRekening(request.getNoRekening());
        ke.setEmail(request.getEmail());
        ke.setNoHandphone(request.getNoHandphone());
        ke.setNonik(request.getNonik());
        ke.setNokk(request.getNokk());
        ke.setNonpwp(request.getNonpwp());
        ke.setNoBpjsKesehatan(request.getNoBpjsKesehatan());
        ke.setNoBpjsTenagaKerja(request.getNoBpjsTenagaKerja());
        ke.setGolonganDarah(request.getGolonganDarah());
        ke.setNamaAyahKandung(request.getNamaAyahKandung());
        ke.setNamaIbuKandung(request.getNamaIbuKandung());
        ke.setKeluargaYangDihubungi(request.getKeluargaYangDihubungi());
        ke.setNamaKeluargaYangDihubungi(request.getNamaKeluargaYangDihubungi());
        ke.setAlamatDomisili(request.getAlamatDomisili());
        ke.setNoHpKeluarga(request.getNoHpKeluarga());
        ke.setRekeningAtasNama(request.getRekeningAtasNama());

        ke.setIsActive(true);
        ke.setFileUploadId(fileCv.getFileUploadId());
        ke.setCreatedDate(new Date());

        try {
            ke.setPendidikanTerakhir(objectMapper.writeValueAsString(request.getPendidikanTerakhir()));
            ke.setRiwayatPekerjaan(objectMapper.writeValueAsString(request.getRiwayatPekerjaan()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return ke;
    }

    private KaryawanResponse getKaryawanResponse(KaryawanEntity ke) {
        List<DetailPendidikanResponse> listDetailPendidikan = null;
        List<DetailRiwayatPekerjaanResponse> listDetailPekerjaan = null;
        try {
            listDetailPekerjaan = Arrays.asList(objectMapper.readValue(ke.getRiwayatPekerjaan(), DetailRiwayatPekerjaanResponse[].class));
            listDetailPendidikan = Arrays.asList(objectMapper.readValue(ke.getPendidikanTerakhir(), DetailPendidikanResponse[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        KaryawanResponse response = new KaryawanResponse();
        response.setKaryawanId(ke.getKaryawanId());
        response.setAgama(ke.getAgama());
        response.setAlamatRumah(ke.getAlamatRumah());

        response.setBankId(ke.getBankId());
        BankMasterEntity bme = bankMasterRepository.findByBankId(ke.getBankId());
        if(bme!=null){
            response.setBankName(bme.getBankName());
        }

        response.setDtmUpdate(ke.getDtmUpdate());
        response.setEmail(ke.getEmail());
        response.setGender(ke.getGender());
        response.setIsActive(ke.getIsActive());
        response.setKaryawanName(ke.getKaryawanName());
        response.setKaryawanNip(ke.getKaryawanNip());
        response.setNoBpjsKesehatan(ke.getNoBpjsKesehatan());
        response.setNoBpjsTenagaKerja(ke.getNoBpjsTenagaKerja());
        response.setNoHandphone(ke.getNoHandphone());
        response.setNokk(ke.getNokk());
        response.setNonik(ke.getNonik());
        response.setNonpwp(ke.getNonpwp());
        response.setNoRekening(ke.getNoRekening());
        response.setPendidikanTerakhir(listDetailPendidikan);
        response.setStatusNikah(ke.getStatusNikah());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));
        response.setTempatLahir(ke.getTempatLahir());
        response.setUsrUpdate(ke.getUsrUpdate());
        response.setFileUploadId(ke.getFileUploadId());
        response.setGolonganDarah(ke.getGolonganDarah());
        response.setNamaAyahKandung(ke.getNamaAyahKandung());
        response.setNamaIbuKandung(ke.getNamaIbuKandung());
        response.setKeluargaYangDihubungi(ke.getKeluargaYangDihubungi());
        response.setNamaKeluargaYangDihubungi(ke.getNamaKeluargaYangDihubungi());
        response.setAlamatDomisili(ke.getAlamatDomisili());
        response.setNoHpKeluarga(ke.getNoHpKeluarga());
        response.setRekeningAtasNama(ke.getRekeningAtasNama());
        response.setRiwayatPekerjaan(listDetailPekerjaan);
        response.setSuratPeringatan(ke.getSuratPeringatan());
        response.setTanggalSuratPeringatan(HrisConstant.formatDate(ke.getTanggalSuratPeringatan()));

        return response;
    }

    private KaryawanByNipResponse getKaryawanByNipResponse(KaryawanEntity keExist) {
        List<DetailPendidikanResponse> listDetailPendidikan = null;
        List<DetailRiwayatPekerjaanResponse> listDetailPekerjaan = null;
        try {
            listDetailPekerjaan = Arrays.asList(objectMapper.readValue(keExist.getRiwayatPekerjaan(), DetailRiwayatPekerjaanResponse[].class));
            listDetailPendidikan = Arrays.asList(objectMapper.readValue(keExist.getPendidikanTerakhir(), DetailPendidikanResponse[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        KaryawanByNipResponse response = new KaryawanByNipResponse();
        response.setKaryawanNip(keExist.getKaryawanNip());
        response.setKaryawanName(keExist.getKaryawanName());
        response.setNonik(keExist.getNonik());
        response.setTempatLahir(keExist.getTempatLahir());
        response.setTanggalLahir(HrisConstant.formatDate(keExist.getTanggalLahir()));
        response.setAgama(keExist.getAgama());
        response.setAlamatRumah(keExist.getAlamatRumah());

        response.setBankId(keExist.getBankId());
        BankMasterEntity bme = bankMasterRepository.findByBankId(keExist.getBankId());
        if(bme!=null){
            response.setBankName(bme.getBankName());
        }

        response.setDtmUpdate(keExist.getDtmUpdate());
        response.setEmail(keExist.getEmail());
        response.setGender(keExist.getGender());
        response.setIsActive(keExist.getIsActive());
        response.setNoBpjsKesehatan(keExist.getNoBpjsKesehatan());
        response.setNoBpjsTenagaKerja(keExist.getNoBpjsTenagaKerja());
        response.setNoHandphone(keExist.getNoHandphone());
        response.setNokk(keExist.getNokk());
        response.setNonpwp(keExist.getNonpwp());
        response.setNoRekening(keExist.getNoRekening());
        response.setPendidikanTerakhir(listDetailPendidikan);
        response.setStatusNikah(keExist.getStatusNikah());
        response.setUsrUpdate(keExist.getUsrUpdate());
        response.setFileUploadId(keExist.getFileUploadId());
        response.setGolonganDarah(keExist.getGolonganDarah());
        response.setNamaAyahKandung(keExist.getNamaAyahKandung());
        response.setNamaIbuKandung(keExist.getNamaIbuKandung());
        response.setKeluargaYangDihubungi(keExist.getKeluargaYangDihubungi());
        response.setNamaKeluargaYangDihubungi(keExist.getNamaKeluargaYangDihubungi());
        response.setAlamatDomisili(keExist.getAlamatDomisili());
        response.setNoHpKeluarga(keExist.getNoHpKeluarga());
        response.setRekeningAtasNama(keExist.getRekeningAtasNama());
        response.setRiwayatPekerjaan(listDetailPekerjaan);
        response.setSuratPeringatan(keExist.getSuratPeringatan());
        response.setTanggalSuratPeringatan(HrisConstant.formatDate(keExist.getTanggalSuratPeringatan()));

        return response;
    }

    private void extracted(List<ListKaryawanResponse> listKaryawanResponse, KaryawanEntity ke) {
        List<DetailPendidikanResponse> listDetailPendidikan = null;
        List<DetailRiwayatPekerjaanResponse> listDetailPekerjaan = null;
        try {
            listDetailPekerjaan = Arrays.asList(objectMapper.readValue(ke.getRiwayatPekerjaan(), DetailRiwayatPekerjaanResponse[].class));
            listDetailPendidikan = Arrays.asList(objectMapper.readValue(ke.getPendidikanTerakhir(), DetailPendidikanResponse[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        ListKaryawanResponse response = new ListKaryawanResponse();
        response.setKaryawanId(ke.getKaryawanId());
        response.setKaryawanNip(ke.getKaryawanNip());
        response.setKaryawanName(ke.getKaryawanName());
        response.setIsActive(ke.getIsActive());
        response.setAgama(ke.getAgama());
        response.setAlamatRumah(ke.getAlamatRumah());

        response.setBankId(ke.getBankId());
        BankMasterEntity bme = bankMasterRepository.findByBankId(ke.getBankId());
        if(bme!=null){
            response.setBankName(bme.getBankName());
        }

        response.setDtmUpdate(ke.getDtmUpdate());
        response.setEmail(ke.getEmail());
        response.setGender(ke.getGender());
        response.setNoBpjsKesehatan(ke.getNoBpjsKesehatan());
        response.setNoBpjsTenagaKerja(ke.getNoBpjsTenagaKerja());
        response.setNoHandphone(ke.getNoHandphone());
        response.setNokk(ke.getNokk());
        response.setNonik(ke.getNonik());
        response.setNonpwp(ke.getNonpwp());
        response.setNoRekening(ke.getNoRekening());
        response.setPendidikanTerakhir(listDetailPendidikan);
        response.setStatusNikah(ke.getStatusNikah());
        response.setTanggalLahir(HrisConstant.formatDate(ke.getTanggalLahir()));
        response.setTempatLahir(ke.getTempatLahir());
        response.setUsrUpdate(ke.getUsrUpdate());
        response.setFileUploadId(ke.getFileUploadId());
        response.setGolonganDarah(ke.getGolonganDarah());
        response.setNamaAyahKandung(ke.getNamaAyahKandung());
        response.setNamaIbuKandung(ke.getNamaIbuKandung());
        response.setKeluargaYangDihubungi(ke.getKeluargaYangDihubungi());
        response.setNamaKeluargaYangDihubungi(ke.getNamaKeluargaYangDihubungi());
        response.setAlamatDomisili(ke.getAlamatDomisili());
        response.setNoHpKeluarga(ke.getNoHpKeluarga());
        response.setRekeningAtasNama(ke.getRekeningAtasNama());
        response.setRiwayatPekerjaan(listDetailPekerjaan);
        response.setSuratPeringatan(ke.getSuratPeringatan());
        response.setTanggalSuratPeringatan(HrisConstant.formatDate(ke.getTanggalSuratPeringatan()));

        List<KontrakKerjaEntity> listKkExistCekPeriod = kontrakKerjaRepository.findByKaryawanNip(ke.getKaryawanNip());
        for(KontrakKerjaEntity kke : listKkExistCekPeriod){

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
            response.setPeriodKontrak(kontrakKerjaRepository.getMaxPeriodKontrakForListKaryawan(ke.getKaryawanNip()));
            response.setNoKontrak(kke.getKontrakKode());
            response.setKontrakId(kke.getKontrakId());
        }

        listKaryawanResponse.add(response);
    }

    private KaryawanHistoryEntity getKaryawanHistoryEntity(KaryawanEntity entity) {
        KaryawanHistoryEntity keH = new KaryawanHistoryEntity();
        keH.setKaryawanHistoryId(UUID.randomUUID());
        keH.setKaryawanId(entity.getKaryawanId());
        keH.setKaryawanNip(entity.getKaryawanNip());
        keH.setKaryawanName(entity.getKaryawanName());
        keH.setTempatLahir(entity.getTempatLahir());
        keH.setTanggalLahir(entity.getTanggalLahir());
        keH.setAgama(entity.getAgama());
        keH.setGender(entity.getGender());
        keH.setStatusNikah(entity.getStatusNikah());
        keH.setAlamatRumah(entity.getAlamatRumah());
        keH.setBankId(entity.getBankId());
        keH.setNoRekening(entity.getNoRekening());
        keH.setEmail(entity.getEmail());
        keH.setNoHandphone(entity.getNoHandphone());
        keH.setNonik(entity.getNonik());
        keH.setNokk(entity.getNokk());
        keH.setNonpwp(entity.getNonpwp());
        keH.setNoBpjsKesehatan(entity.getNoBpjsKesehatan());
        keH.setNoBpjsTenagaKerja(entity.getNoBpjsTenagaKerja());
        keH.setGolonganDarah(entity.getGolonganDarah());
        keH.setNamaAyahKandung(entity.getNamaAyahKandung());
        keH.setNamaIbuKandung(entity.getNamaIbuKandung());
        keH.setKeluargaYangDihubungi(entity.getKeluargaYangDihubungi());
        keH.setNamaKeluargaYangDihubungi(entity.getNamaKeluargaYangDihubungi());
        keH.setAlamatDomisili(entity.getAlamatDomisili());
        keH.setNoHpKeluarga(entity.getNoHpKeluarga());
        keH.setRekeningAtasNama(entity.getRekeningAtasNama());

        keH.setIsActive(entity.getIsActive());
        keH.setFileUploadId(entity.getFileUploadId());
        keH.setCreatedDate(entity.getCreatedDate());
        keH.setSuratPeringatan(entity.getSuratPeringatan());
        keH.setTanggalSuratPeringatan(entity.getTanggalSuratPeringatan());
        keH.setDtmUpdate(entity.getDtmUpdate());
        keH.setUsrUpdate(entity.getUsrUpdate());
        keH.setFlagAction("UPDATE");

        try {
            keH.setPendidikanTerakhir(objectMapper.writeValueAsString(entity.getPendidikanTerakhir()));
            keH.setRiwayatPekerjaan(objectMapper.writeValueAsString(entity.getRiwayatPekerjaan()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return keH;
    }
}
