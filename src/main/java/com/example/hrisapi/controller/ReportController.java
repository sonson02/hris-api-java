package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedReportTagihanGajiResponse;
import com.example.hrisapi.api.base.PaginatedReportJamsosResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.response.ReportJamsosResponse;
import com.example.hrisapi.dto.response.ReportSPResponse;
import com.example.hrisapi.dto.response.ReportTagihanGajiResponse;
import com.example.hrisapi.service.ReportService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/tagihan_gaji")
    public ResponseEntity<JsonBaseResponse<PaginatedReportTagihanGajiResponse<ReportTagihanGajiResponse>>> reportTagihanGaji(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, value = "karyawan_name") String name,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, value = "periode") String periode,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedReportTagihanGajiResponse<ReportTagihanGajiResponse>>(
                System.currentTimeMillis(),
                reportService.getReportTagihanGaji(nip, name, unitId, periode, page, size)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/karyawan_sp")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<ReportSPResponse>>> reportKaryawanSP(
            @RequestParam(required = false, value = "karyawan_name") String name,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, value = "periode") String periode,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<ReportSPResponse>>(
                System.currentTimeMillis(),
                reportService.getReportSP(name, unitId, periode, page, size)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/jamsos")
    public ResponseEntity<JsonBaseResponse<PaginatedReportJamsosResponse<ReportJamsosResponse>>> reportJamsos(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedReportJamsosResponse<ReportJamsosResponse>>(
                System.currentTimeMillis(),
                reportService.getReportJamsos(page, size)
        );
        return ResponseEntity.ok(body);
    }

}
