package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedReportResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.response.ReportTagihanGajiResponse;
import com.example.hrisapi.service.ReportTagihanGajiService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private ReportTagihanGajiService reportTagihanGajiService;

    @GetMapping("/tagihan_gaji")
    public ResponseEntity<JsonBaseResponse<PaginatedReportResponse<ReportTagihanGajiResponse>>> reportTagihanGaji(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, value = "periode") String periode,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedReportResponse<ReportTagihanGajiResponse>>(
                System.currentTimeMillis(),
                reportTagihanGajiService.getReportTagihanGaji(nip, unitId, periode, page, size)
        );
        return ResponseEntity.ok(body);
    }

}
