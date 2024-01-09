package com.example.hrisapi.service;

import com.example.hrisapi.api.base.PaginatedDashboardResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.api.exception.NikAlreadyExistException;
import com.example.hrisapi.api.exception.NipAlreadyExistException;
import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.dto.request.KaryawanRequest;
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

            if(k.getKaryawanNip().equalsIgnoreCase(request.getKaryawanNip())){
                throw new NipAlreadyExistException();
            }

            if(k.getNonik().equalsIgnoreCase(request.getNonik())){
                throw new NikAlreadyExistException();
            }
        }
        KaryawanEntity ke = getKaryawanEntity(request);

        KaryawanResponse response = getKaryawanResponse(ke);
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
        ke.setTempatTinggal(request.getTempatTinggal());
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
        ke.setJurusan(request.getJurusan());
        ke.setAsalSekolah(request.getAsalSekolah());
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
        ke.setGaji(0L);
        ke.setUangTelekomunikasi(0D);
        ke.setUangMakan(0D);
        ke.setDtmUpdate(new Date());

        try {
            ke.setPendidikanTerakhir(objectMapper.writeValueAsString(request.getPendidikanTerakhir()));
            ke.setRiwayatPekerjaan(objectMapper.writeValueAsString(request.getRiwayatPekerjaan()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        karyawanRepository.save(ke);
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
        response.setAsalSekolah(ke.getAsalSekolah());
        response.setBankId(ke.getBankId());
        response.setDtmUpdate(ke.getDtmUpdate());
        response.setEmail(ke.getEmail());
        response.setGaji(ke.getGaji());
        response.setGender(ke.getGender());
        response.setIsActive(ke.getIsActive());
        response.setJurusan(ke.getJurusan());
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
        response.setTempatTinggal(ke.getTempatTinggal());
        response.setTempatTugasId(ke.getTempatTugasId());
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));
        response.setTglMasukKerja(HrisConstant.formatDate(ke.getTglMasukKerja()));
        response.setTipeTunjangan(ke.getTipeTunjangan());
        response.setUangTelekomunikasi(ke.getUangTelekomunikasi());
        response.setUnitId(ke.getUnitId());
        response.setUsrUpdate(ke.getUsrUpdate());
        response.setJabatanId(ke.getJabatanId());
        response.setFileUploadId(ke.getFileUploadId());
        response.setUangMakan(ke.getUangMakan());
        response.setGolonganDarah(ke.getGolonganDarah());
        response.setNamaAyahKandung(ke.getNamaAyahKandung());
        response.setNamaIbuKandung(ke.getNamaIbuKandung());
        response.setKeluargaYangDihubungi(ke.getKeluargaYangDihubungi());
        response.setNamaKeluargaYangDihubungi(ke.getNamaKeluargaYangDihubungi());
        response.setAlamatDomisili(ke.getAlamatDomisili());
        response.setNoHpKeluarga(ke.getNoHpKeluarga());
        response.setRekeningAtasNama(ke.getRekeningAtasNama());
        response.setRiwayatPekerjaan(listDetailPekerjaan);

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

        try {
            keExist.setPendidikanTerakhir(objectMapper.writeValueAsString(request.getPendidikanTerakhir()));
            keExist.setRiwayatPekerjaan(objectMapper.writeValueAsString(request.getRiwayatPekerjaan()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

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

        JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(keExist.getJabatanId());
        if(jme!=null){
            response.setTunjanganJabatan(jme.getTunjangan());
        } else {
            response.setTunjanganJabatan(0D);
        }

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
        response.setTempatTinggal(keExist.getTempatTinggal());
        response.setTanggalLahir(HrisConstant.formatDate(keExist.getTanggalLahir()));
        response.setAgama(keExist.getAgama());
        response.setAlamatRumah(keExist.getAlamatRumah());
        response.setAsalSekolah(keExist.getAsalSekolah());
        response.setBankId(keExist.getBankId());
        response.setDtmUpdate(keExist.getDtmUpdate());
        response.setEmail(keExist.getEmail());
        response.setGaji(keExist.getGaji());
        response.setGender(keExist.getGender());
        response.setIsActive(keExist.getIsActive());
        response.setJurusan(keExist.getJurusan());
        response.setNoBpjsKesehatan(keExist.getNoBpjsKesehatan());
        response.setNoBpjsTenagaKerja(keExist.getNoBpjsTenagaKerja());
        response.setNoHandphone(keExist.getNoHandphone());
        response.setNokk(keExist.getNokk());
        response.setNonpwp(keExist.getNonpwp());
        response.setNoRekening(keExist.getNoRekening());
        response.setPendidikanTerakhir(listDetailPendidikan);
        response.setStatusNikah(keExist.getStatusNikah());
        response.setTempatTugasId(keExist.getTempatTugasId());
        response.setTglHabisKontrak(HrisConstant.formatDate(keExist.getTglHabisKontrak()));
        response.setTglMasukKerja(HrisConstant.formatDate(keExist.getTglMasukKerja()));
        response.setTipeTunjangan(keExist.getTipeTunjangan());
        response.setUangTelekomunikasi(keExist.getUangTelekomunikasi());
        response.setUnitId(keExist.getUnitId());
        response.setUsrUpdate(keExist.getUsrUpdate());
        response.setJabatanId(keExist.getJabatanId());
        response.setFileUploadId(keExist.getFileUploadId());
        response.setUangMakan(keExist.getUangMakan());
        response.setGolonganDarah(keExist.getGolonganDarah());
        response.setNamaAyahKandung(keExist.getNamaAyahKandung());
        response.setNamaIbuKandung(keExist.getNamaIbuKandung());
        response.setKeluargaYangDihubungi(keExist.getKeluargaYangDihubungi());
        response.setNamaKeluargaYangDihubungi(keExist.getNamaKeluargaYangDihubungi());
        response.setAlamatDomisili(keExist.getAlamatDomisili());
        response.setNoHpKeluarga(keExist.getNoHpKeluarga());
        response.setRekeningAtasNama(keExist.getRekeningAtasNama());
        response.setRiwayatPekerjaan(listDetailPekerjaan);

        return response;
    }

    public PaginatedDashboardResponse<ListKaryawanResponse> getKaryawanDashboard(Integer page, Integer size){
        List<KaryawanEntity> listKaryawanEntity = karyawanRepository.getKaryawanDashboard();
        List<ListKaryawanResponse> listKaryawanResponse = new ArrayList<>();

        for(KaryawanEntity ke : listKaryawanEntity){
            extracted(listKaryawanResponse, ke);
        }

        var count30Days = karyawanRepository.getKaryawanDashboard_30_Days();
        var count60Days = karyawanRepository.getKaryawanDashboard_60_Days();
        var count90Days = karyawanRepository.getKaryawanDashboard_90_Days();

        return (PaginatedDashboardResponse<ListKaryawanResponse>) HrisConstant.extractPaginationDashboard(
                page,
                size,
                listKaryawanResponse,
                count30Days,
                count60Days,
                count90Days
        );
    }

    private void extracted(List<ListKaryawanResponse> listKaryawanResponse, KaryawanEntity ke) {
        ListKaryawanResponse response = new ListKaryawanResponse();
        response.setKaryawanId(ke.getKaryawanId());
        response.setKaryawanNip(ke.getKaryawanNip());
        response.setKaryawanName(ke.getKaryawanName());

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
        response.setIsActive(ke.getIsActive());
        response.setTglHabisKontrak(HrisConstant.formatDate(ke.getTglHabisKontrak()));

        List<KontrakKerjaEntity> listKkExistCekPeriod = kontrakKerjaRepository.findByKaryawanNip(ke.getKaryawanNip());
        for(KontrakKerjaEntity kke : listKkExistCekPeriod){
            response.setPeriodKontrak(kontrakKerjaRepository.getMaxPeriodKontrakForListKaryawan(ke.getKaryawanNip()));
            response.setNoKontrak(kke.getKontrakKode());
            response.setKontrakId(kke.getKontrakId());
        }

        listKaryawanResponse.add(response);
    }

}
