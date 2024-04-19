package com.example.hrisapi.constant;

import com.example.hrisapi.api.base.*;
import lombok.var;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class HrisConstant {

    public static final Double MANAJEMEN_FEE_PERCENTAGE = 0.1;

    public static final Double MANAJEMEN_FEE_PPH_11 = 0.11;

    private static final String PATTERN_DATE_FORMAT = "yyyy-MM-dd";

    private static final String PATTERN_DATE_FORMAT_PKWT_PDF = "dd MMMM yyyy";

    private static final String PATTERN_DATE_FORMAT_SLIP_GAJI_PERIODE = "MMM-yyyy";

    public static final Double BPJS_JAMINAN_PENSIUN_PERCENTAGE = 0.037;

    public static final Double BPJS_TENAGA_KERJA_PERCENTAGE = 0.02;

    public static final Double LIMIT_BAWAH_BPJS_KESEHATAN = 4641854D;

    public static final Double LIMIT_ATAS_BPJS_KESEHATAN = 12000000D;

    public static final Double BPJS_KESEHATAN_PERCENTAGE = 0.01;

    public static final Double BPJS_KK_JKK = 0.0174;

    public static final Double BPJS_KK_JKM = 0.0003;

    public static final Double BPJS_KK_JHT = 0.037;

    public static final Double BPJS_KK_B_PEG = 0.02;

    public static final Double LIMIT_BAWAH_BPJS_KS_BEBAN_PEGAWAI = 4901798D;

    public static final Double BPJS_KS_BEBAN_PEGAWAI_PERCENTAGE = 0.04;

    public static final Double LIMIT_BPJS_TK = 9559600D;

    public static PaginatedResponse<?> extractPaginationList(int page, int size, final List<?> listMaster) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .build();
    }

    public static String formatDate(Date requestDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_FORMAT);
        String responseDate = "";
        if (requestDate != null) {
            responseDate = sdf.format(requestDate);
        }

        return responseDate;
    }

    public static String formatDatePkwtPdf(Date requestDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_FORMAT_PKWT_PDF);
        String responseDate = "";
        if (requestDate != null) {
            responseDate = sdf.format(requestDate);
        }

        return responseDate;
    }

    public static String formatDateSlipGajiPeriode(Date requestDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_DATE_FORMAT_SLIP_GAJI_PERIODE);
        String responseDate = "";
        if (requestDate != null) {
            responseDate = sdf.format(requestDate);
        }

        return responseDate;
    }

    public static String decimalFormatIdr(Double input) {
        DecimalFormat df = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMAN));
        String hasil = df.format(input);
        return hasil;
    }

    public static String decimalFormatIdrGaji(Long input) {
        DecimalFormat df = new DecimalFormat("#,###.##", new DecimalFormatSymbols(Locale.GERMAN));
        String hasil = df.format(input);
        return hasil;
    }

    public static Integer getBulanPeriode(String input){
        int bulan=0;
        String[] period = input.split("/");
        for(int i=0; i< period.length; i++) {
            bulan = Integer.valueOf(period[0]);
        }
        return bulan;
    }

    public static Integer getTahunPeriode(String input){
        int tahun=0;
        String[] period = input.split("/");
        for(int i=0; i< period.length; i++) {
            tahun = Integer.valueOf(period[1]);
        }
        return tahun;
    }

    public static PaginatedReportTagihanGajiResponse<?> extractPaginationListReportTagihanGaji(int page, int size, final List<?> listMaster,
                                                                                               Double totalGaji, Double totalTunjangan, Double totalGajiDibayar,
                                                                                               Double totalManajemenFee, Double totalTagihanGaji, Double totalUangMakan,
                                                                                               Double totalTunjanganKhusus, Double totalTunjanganVariabel,
                                                                                               Double totalTunjanganKomunikasi, Double totalPph11) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedReportTagihanGajiResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .totalGaji(totalGaji)
                .totalTunjangan(totalTunjangan)
                .totalGajiDibayar(totalGajiDibayar)
                .totalManajemenFee(totalManajemenFee)
                .totalTagihanGaji(totalTagihanGaji)
                .totalUangMakan(totalUangMakan)
                .totalTunjanganKhusus(totalTunjanganKhusus)
                .totalTunjanganVariabel(totalTunjanganVariabel)
                .totalTunjanganKomunikasi(totalTunjanganKomunikasi)
                .totalPph11(totalPph11)
                .build();
    }

    public static String angkaToTerbilang(Double angka){
        String[] huruf={"","Satu","Dua","Tiga","Empat","Lima","Enam","Tujuh","Delapan","Sembilan","Sepuluh","Sebelas"};
        if(angka < 12)
            return huruf[angka.intValue()];
        if(angka >=12 && angka <= 19)
            return huruf[angka.intValue() % 10] + " Belas";
        if(angka >= 20 && angka <= 99)
            return angkaToTerbilang(angka / 10) + " Puluh " + huruf[angka.intValue() % 10];
        if(angka >= 100 && angka <= 199)
            return "Seratus " + angkaToTerbilang(angka % 100);
        if(angka >= 200 && angka <= 999)
            return angkaToTerbilang(angka / 100) + " Ratus " + angkaToTerbilang(angka % 100);
        if(angka >= 1000 && angka <= 1999)
            return "Seribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 2000 && angka <= 999999)
            return angkaToTerbilang(angka / 1000) + " Ribu " + angkaToTerbilang(angka % 1000);
        if(angka >= 1000000 && angka <= 999999999)
            return angkaToTerbilang(angka / 1000000) + " Juta " + angkaToTerbilang(angka % 1000000);
        if(angka >= 1000000000 && angka <= 999999999999L)
            return angkaToTerbilang(angka / 1000000000) + " Milyar " + angkaToTerbilang(angka % 1000000000);
        if(angka >= 1000000000000L && angka <= 999999999999999L)
            return angkaToTerbilang(angka / 1000000000000L) + " Triliun " + angkaToTerbilang(angka % 1000000000000L);
        if(angka >= 1000000000000000L && angka <= 999999999999999999L)
            return angkaToTerbilang(angka / 1000000000000000L) + " Quadrilyun " + angkaToTerbilang(angka % 1000000000000000L);
        return "";
    }

    public static PaginatedDashboardResponse<?> extractPaginationDashboard(int page, int size, final List<?> listMaster,
                                                                           Integer count30Days, Integer count60Days, Integer count90Days,
                                                                           Integer totalKaryawanActive, Integer totalKaryawanBaru, Integer totalKaryawanBerhenti) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedDashboardResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .total30Days(count30Days)
                .total60Days(count60Days)
                .total90Days(count90Days)
                .totalKaryawanActive(totalKaryawanActive)
                .totalKaryawanBaru(totalKaryawanBaru)
                .totalKaryawanBerhenti(totalKaryawanBerhenti)
                .build();
    }

    public static PaginatedSlipGajiByNipResponse<?> extractPaginationSlipGajiByNip(final List<?> listMaster) {
        final var paginatedList = new ArrayList<>();

        return PaginatedSlipGajiByNipResponse
                .builder()
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .build();
    }

    public static PaginatedReportJamsosResponse<?> extractPaginationListReportJamsos(int page, int size, final List<?> listMaster,
                                                                               Double totalGaji, Double totalGajiTambahUangMakan, Double totalBpjsKKJkk,
                                                                               Double totalBpjsKKJkm, Double totalBpjsKKJht, Double totalBpjsKKJkkJkmJht,
                                                                               Double totalBpjsKKBebanPegawai, Double totalBpjsKSBebanPerusahaan,
                                                                               Double totalBpjsKSBebanPegawai, Double totalBpjsTKBebanPerusahaan, Double totalBpjsTKBebanPegawai,
                                                                                     Double totalPphPasal21, Double totalGajiDiterima,
                                                                                     Double subTotalA, Double managementFee5, Double subTotalAB, Double ppn11,
                                                                                     Double grandTotal, String terbilang) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedReportJamsosResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .totalGaji(totalGaji)
                .totalGajiTambahUangMakan(totalGajiTambahUangMakan)
                .totalBpjsKkJkk(totalBpjsKKJkk)
                .totalBpjsKkJkm(totalBpjsKKJkm)
                .totalBpjsKkJht(totalBpjsKKJht)
                .totalBpjsKkJkkJkmJht(totalBpjsKKJkkJkmJht)
                .totalBpjsKkBebanPegawai(totalBpjsKKBebanPegawai)
                .totalBpjsKsBebanPerusahaan(totalBpjsKSBebanPerusahaan)
                .totalBpjsKsBebanPegawai(totalBpjsKSBebanPegawai)
                .totalBpjsTkBebanPerusahaan(totalBpjsTKBebanPerusahaan)
                .totalBpjsTkBebanPegawai(totalBpjsTKBebanPegawai)
                .totalPphPasal21(totalPphPasal21)
                .totalGajiDiterima(totalGajiDiterima)
                .subTotalA(subTotalA)
                .managementFee5(managementFee5)
                .subTotalAB(subTotalAB)
                .ppn11(ppn11)
                .grandTotal(grandTotal)
                .terbilang(terbilang)
                .build();
    }

    public static PaginatedReportKompensasiResponse<?> extractPaginationListReportKompensasi(int page, int size, final List<?> listMaster,
                                                                                     Double totalKompensasiDiterima, Double totalManagementFee, Double totalTotal,
                                                                                             Double pph11, Double totalRekap, String terbilang) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedReportKompensasiResponse
                .builder()
                .page(page)
                .size(size)
                .totalRecord(listMaster.size())
                .data(paginatedList)
                .totalKompensasiDiterima(totalKompensasiDiterima)
                .totalManagementFee(totalManagementFee)
                .totalTotal(totalTotal)
                .pph11(pph11)
                .totalRekap(totalRekap)
                .terbilang(terbilang)
                .build();
    }
}
