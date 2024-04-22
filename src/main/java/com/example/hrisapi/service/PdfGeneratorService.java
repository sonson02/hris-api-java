package com.example.hrisapi.service;

import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.entity.*;
import com.example.hrisapi.repository.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import lombok.var;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
public class PdfGeneratorService{

    @Autowired
    private KontrakKerjaRepository kontrakKerjaRepository;

    @Autowired
    private KaryawanRepository karyawanRepository;

    @Autowired
    private UnitMasterRepository unitMasterRepository;

    @Autowired
    private TempatTugasMasterRepository tempatTugasMasterRepository;

    @Autowired
    private JabatanMasterRepository jabatanMasterRepository;

    @Autowired
    private ReportService reportService;

    @Autowired
    private MasterPph21Repository pph21Repository;

    public ByteArrayOutputStream generatePdf(String html) {
        PdfWriter pdfWriter = null;
        Document document;
        try {
            document = new Document();
            document.setPageSize(PageSize.LETTER);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);

            document.open();

            XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
            xmlWorkerHelper.getDefaultCssResolver(true);
            xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(html));
            document.close();

            return baos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpEntity<byte[]> createPdfPkwt(UUID kontrakId) throws IOException {

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("templates/template-pkwt.html");

        //get data
        KontrakKerjaEntity kontrakExist = kontrakKerjaRepository.findByKontrakId(kontrakId);
        KaryawanEntity karyawanExist = karyawanRepository.findByKaryawanNip(kontrakExist.getKaryawanNip());

        VelocityContext context = new VelocityContext();
        context.put("noKontrak", kontrakExist.getKontrakKode());
        context.put("karyawanName", karyawanExist.getKaryawanName());
        context.put("nik", karyawanExist.getNonik());
        context.put("jenisKelamin", karyawanExist.getGender());
        context.put("tanggalLahir", HrisConstant.formatDatePkwtPdf(karyawanExist.getTanggalLahir()));
        context.put("alamat", karyawanExist.getAlamatRumah());

        UnitMasterEntity ume = unitMasterRepository.findByUnitId(kontrakExist.getUnitId());
        context.put("unitBisnis", ume.getUnitName());
        context.put("requestNo", kontrakExist.getRequestNo());
        context.put("requestDate", HrisConstant.formatDatePkwtPdf(kontrakExist.getRequestDate()));

        context.put("tglMasukKerja", HrisConstant.formatDatePkwtPdf(kontrakExist.getTglMasukKerja()));
        context.put("tglHabisKontrak", HrisConstant.formatDatePkwtPdf(kontrakExist.getTglHabisKontrak()));

        var gaji = kontrakExist.getGaji();
        context.put("gajiPokok", HrisConstant.decimalFormatIdrGaji(gaji));
        context.put("totalPerBulan", HrisConstant.decimalFormatIdrGaji(gaji));
        context.put("terbilang", HrisConstant.angkaToTerbilang(Double.valueOf(gaji)));

        context.put("uangMakan", HrisConstant.decimalFormatIdr(Double.valueOf(kontrakExist.getUangMakan())));

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos = generatePdf(writer.toString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = pkwt.pdf");
        header.setContentLength(baos.toByteArray().length);

        return new HttpEntity<byte[]>(baos.toByteArray(), header);
    }

    public HttpEntity<byte[]> createPdfSlipGaji(String karyawanNip) throws IOException {

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("templates/template-slip-gaji.html");

        //get data
        KaryawanEntity karyawanExist = karyawanRepository.findByKaryawanNip(karyawanNip);
        KontrakKerjaEntity kkeExist = kontrakKerjaRepository.getKaryawanNipAndIsActive(karyawanExist.getKaryawanNip());
        VelocityContext context = new VelocityContext();

        context.put("periode", "2024");
        context.put("karyawanName", karyawanExist.getKaryawanName());
        context.put("karyawanNip", karyawanExist.getKaryawanNip());
        context.put("status", "OS");

        JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(kkeExist.getJabatanId());
        if(jme!=null) {
            context.put("jabatan", jme.getJabatanName());
        }

        TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(kkeExist.getTempatTugasId());
        if(ttme!=null){
            context.put("tempatTugas", ttme.getNamaProyek());
        }

        //kolom I, II
        var gaji = kkeExist.getGaji();
        context.put("gajiPokok", HrisConstant.decimalFormatIdrGaji(gaji));

        var uangMakan = kkeExist.getUangMakan();
        context.put("uangMakan", HrisConstant.decimalFormatIdr(Double.valueOf(uangMakan)));

        var tunjangan = kkeExist.getTunjangan();
        context.put("tunjangan", HrisConstant.decimalFormatIdr(Double.valueOf(tunjangan)));

        var penghasilan = gaji + uangMakan + tunjangan;
        context.put("penghasilan", HrisConstant.decimalFormatIdr(Double.valueOf(penghasilan)));

        //kolom III. Pajak
        String ter = reportService.jenisTerForPph21(karyawanExist.getStatusNikah());
        MasterPph21Entity masterPph21Ter = pph21Repository.getNominalTer(ter, Double.valueOf(gaji));
        Double nominalTer = masterPph21Ter.getPph21Ter();

        var pph21 = gaji * nominalTer;
        context.put("pph21", HrisConstant.decimalFormatIdr(Double.valueOf(pph21)));
        context.put("pph21perusahaan",0);

        var sisaPajak = pph21;
        context.put("sisaPajak", HrisConstant.decimalFormatIdr(Double.valueOf(sisaPajak)));

        //kolom IV. Iuran
        var bpjsJaminanPensiun = gaji * HrisConstant.BPJS_JAMINAN_PENSIUN_PERCENTAGE;
        context.put("jaminanPensiun", HrisConstant.decimalFormatIdr(bpjsJaminanPensiun));

        var bpjsTenagaKerja = gaji * HrisConstant.BPJS_TENAGA_KERJA_PERCENTAGE;
        context.put("ketenagakerjaan", HrisConstant.decimalFormatIdr(bpjsTenagaKerja));

        var bpjsKesehatan=0D;
        if(gaji<=HrisConstant.LIMIT_BAWAH_BPJS_KESEHATAN){
            bpjsKesehatan = HrisConstant.LIMIT_BAWAH_BPJS_KESEHATAN * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
        } else if(gaji<=HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN){
            bpjsKesehatan = gaji * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
        } else if(gaji>=HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN){
            bpjsKesehatan = HrisConstant.LIMIT_ATAS_BPJS_KESEHATAN * HrisConstant.BPJS_KESEHATAN_PERCENTAGE;
        }
        context.put("kesehatan", HrisConstant.decimalFormatIdr(bpjsKesehatan));

        var jumlahIuran = bpjsJaminanPensiun + bpjsTenagaKerja + bpjsKesehatan;
        context.put("jumlahIuran", HrisConstant.decimalFormatIdr(jumlahIuran));

        var gajiBersih = gaji - jumlahIuran;

        //kolom V. Potongan
        context.put("simpWajib", 0);
        context.put("simpSukarela", 0);
        context.put("koperasi", 0);
        context.put("unitPkbl", 0);
        context.put("jumlahPotongan", 0);

        var diterimaBersih = gajiBersih;
        context.put("diterimaBersih", HrisConstant.decimalFormatIdr(diterimaBersih));

        context.put("date", HrisConstant.formatDatePkwtPdf(new Date()));

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        ByteArrayOutputStream baos;

        baos = generatePdf(writer.toString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = slip-gaji.pdf");
        header.setContentLength(baos.toByteArray().length);

        return new HttpEntity<byte[]>(baos.toByteArray(), header);
    }
}
