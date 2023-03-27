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

        UnitMasterEntity ume = unitMasterRepository.findByUnitId(karyawanExist.getUnitId());
        context.put("unitBisnis", ume.getUnitName());
        context.put("requestNo", kontrakExist.getRequestNo());
        context.put("requestDate", HrisConstant.formatDatePkwtPdf(kontrakExist.getRequestDate()));

        context.put("tglMasukKerja", HrisConstant.formatDatePkwtPdf(karyawanExist.getTglMasukKerja()));
        context.put("tglHabisKontrak", HrisConstant.formatDatePkwtPdf(karyawanExist.getTglHabisKontrak()));

        var gaji = karyawanExist.getGaji();
        context.put("gajiPokok", HrisConstant.decimalFormatPkwtPdt(gaji));

        TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(karyawanExist.getTempatTugasId());
        var tunjangan = ttme.getNominalTunjangan();
        context.put("tunjangan", HrisConstant.decimalFormatPkwtPdt(tunjangan));

        var totalPerbulan = gaji + tunjangan;
        context.put("totalPerBulan", HrisConstant.decimalFormatPkwtPdt(totalPerbulan));
        context.put("terbilang", HrisConstant.angkaToTerbilang(totalPerbulan));

        context.put("uangMakan", HrisConstant.decimalFormatPkwtPdt(karyawanExist.getUangMakan()));

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

        VelocityContext context = new VelocityContext();
        context.put("karyawanName", karyawanExist.getKaryawanName());
        context.put("karyawanNip", karyawanExist.getKaryawanNip());

        JabatanMasterEntity jme = jabatanMasterRepository.findByJabatanId(karyawanExist.getJabatanId());
        if(jme!=null){
            context.put("jabatan", jme.getJabatanName());
        }

        TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(karyawanExist.getTempatTugasId());
        if(ttme!=null){
            context.put("tempatTugas", ttme.getNamaProyek());
        }

        var gaji = karyawanExist.getGaji();
        context.put("gajiPokok", HrisConstant.decimalFormatPkwtPdt(gaji));

        var tunjangan = ttme.getNominalTunjangan();
        context.put("tunjangan", HrisConstant.decimalFormatPkwtPdt(tunjangan));

        context.put("uangMakan", HrisConstant.decimalFormatPkwtPdt(karyawanExist.getUangMakan()));

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
