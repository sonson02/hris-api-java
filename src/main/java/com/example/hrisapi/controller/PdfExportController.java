package com.example.hrisapi.controller;

import com.example.hrisapi.service.PdfGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/api")
public class PdfExportController {

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @GetMapping("/generate/pkwt")
    public HttpEntity<byte[]> generatePdf(
            @RequestParam(required = true, value = "kontrak_id") UUID kontrakId
    ) throws IOException {
        return pdfGeneratorService.createPdf(kontrakId);
    }
}
