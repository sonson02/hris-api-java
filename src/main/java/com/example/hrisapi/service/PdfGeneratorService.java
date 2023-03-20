package com.example.hrisapi.service;

import com.example.hrisapi.constant.HrisConstant;
import com.example.hrisapi.entity.KaryawanEntity;
import com.example.hrisapi.entity.KontrakKerjaEntity;
import com.example.hrisapi.entity.TempatTugasMasterEntity;
import com.example.hrisapi.entity.UnitMasterEntity;
import com.example.hrisapi.repository.KaryawanRepository;
import com.example.hrisapi.repository.KontrakKerjaRepository;
import com.example.hrisapi.repository.TempatTugasMasterRepository;
import com.example.hrisapi.repository.UnitMasterRepository;
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
import java.time.LocalDateTime;
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

    public ByteArrayOutputStream generatePdf(String html) {
        PdfWriter pdfWriter = null;
        Document document = new Document();
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

    public HttpEntity<byte[]> createPdf(UUID kontrakId) throws IOException {

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("templates/helloworld.html");

        //get data
        KontrakKerjaEntity kontrakExist = kontrakKerjaRepository.findByKontrakId(kontrakId);
        KaryawanEntity karyawanExist = karyawanRepository.findByKaryawanNip(kontrakExist.getKaryawanNip());

        VelocityContext context = new VelocityContext();
        context.put("noKontrak", kontrakExist.getKontrakKode());
        context.put("karyawanName", karyawanExist.getKaryawanName());
        context.put("nik", karyawanExist.getNonik());
        context.put("jenisKelamin", karyawanExist.getGender());
        context.put("tanggalLahir", HrisConstant.formatDate(karyawanExist.getTanggalLahir()));
        context.put("alamat", karyawanExist.getAlamatRumah());

        UnitMasterEntity ume = unitMasterRepository.findByUnitId(karyawanExist.getUnitId());
        context.put("unitBisnis", ume.getUnitName());
        context.put("requestNo", kontrakExist.getRequestNo());
        context.put("requestDate", kontrakExist.getRequestDate());

        context.put("tglMasukKerja", HrisConstant.formatDate(karyawanExist.getTglMasukKerja()));
        context.put("tglHabisKontrak", HrisConstant.formatDate(karyawanExist.getTglHabisKontrak()));

        var gaji = karyawanExist.getGaji();
        context.put("gajiPokok", gaji);

        TempatTugasMasterEntity ttme = tempatTugasMasterRepository.findByTempatTugasId(karyawanExist.getTempatTugasId());
        var tunjangan = ttme.getNominalTunjangan();
        context.put("tunjangan", tunjangan);

        var totalPerbulan = gaji + tunjangan;
        context.put("totalPerBulan", totalPerbulan);

        context.put("uangMakan", karyawanExist.getUangMakan());

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos = generatePdf(writer.toString());

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_PDF);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=");
        header.setContentLength(baos.toByteArray().length);

        return new HttpEntity<byte[]>(baos.toByteArray(), header);
    }
}
