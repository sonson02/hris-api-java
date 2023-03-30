package com.example.hrisapi.constant;

import com.example.hrisapi.api.base.PaginatedDashboardResponse;
import com.example.hrisapi.api.base.PaginatedReportResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.api.exception.DataNotFoundException;
import com.example.hrisapi.entity.UnitMasterEntity;
import com.example.hrisapi.repository.UnitMasterRepository;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class HrisConstant {

    public static final Double MANAJEMEN_FEE_PERCENTAGE = 0.1;

    private static final String PATTERN_DATE_FORMAT = "yyyy-MM-dd";

    private static final String PATTERN_DATE_FORMAT_PKWT_PDF = "dd MMMM yyyy";

    private static final String PATTERN_DATE_FORMAT_SLIP_GAJI_PERIODE = "MMM-yyyy";

    public static final Double BPJS_JAMINAN_PENSIUN_PERCENTAGE = 0.037;

    public static final Double BPJS_TENAGA_KERJA_PERCENTAGE = 0.02;

    public static final Double LIMIT_BPJS_KESEHATAN = 4641854D;

    public static final Double LIMIT_GAPOK_BPJS_KESEHATAN = 12000000D;

    public static final Double BPJS_KESEHATAN_PERCENTAGE = 0.01;

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

    public static PaginatedReportResponse<?> extractPaginationListReport(int page, int size, final List<?> listMaster,
                                                                         Double totalGaji, Double totalTunjangan, Double totalGajiDibayar,
                                                                         Double totalManajemenFee, Double totalTagihanGaji) {
        final var fromIndex = (page - 1) * size;
        final var paginatedList = new ArrayList<>();

        if (fromIndex < listMaster.size()) {
            paginatedList.addAll(
                    listMaster.subList(fromIndex, Math.min(fromIndex + size, listMaster.size()))
            );
        }

        return PaginatedReportResponse
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
                                                                           Integer count30Days, Integer count60Days, Integer count90Days) {
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
                .build();
    }
}
