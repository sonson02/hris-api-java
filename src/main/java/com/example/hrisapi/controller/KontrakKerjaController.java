package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.KontrakKerjaRequest;
import com.example.hrisapi.dto.request.StopKontrakRequest;
import com.example.hrisapi.dto.response.KontrakKerjaByNipResponse;
import com.example.hrisapi.dto.response.KontrakKerjaResponse;
import com.example.hrisapi.dto.response.StopKontrakResponse;
import com.example.hrisapi.service.KontrakKerjaService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class KontrakKerjaController {

    @Autowired
    private KontrakKerjaService kontrakKerjaService;

    @GetMapping("/kontrak_kerja")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<KontrakKerjaResponse>>> getListKontrak(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, value = "karyawan_name") String name,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<KontrakKerjaResponse>>(
                System.currentTimeMillis(),
                kontrakKerjaService.getListKontrak(nip, name, unitId, page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/kontrak_kerja")
    public ResponseEntity<JsonBaseResponse<KontrakKerjaResponse>> insertKontrakKerja(
            @RequestBody KontrakKerjaRequest request)
    {
        var body = new JsonBaseResponse<KontrakKerjaResponse>(
                System.currentTimeMillis(),
                kontrakKerjaService.insertKontrak(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/kontrak_kerja")
    public ResponseEntity<JsonBaseResponse<KontrakKerjaResponse>> updateKontrakKerja(
            @RequestBody KontrakKerjaRequest request)
    {
        var body = new JsonBaseResponse<KontrakKerjaResponse>(
                System.currentTimeMillis(),
                kontrakKerjaService.updateKontrak(request)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/kontrak_kerja/detail")
    public ResponseEntity<JsonBaseResponse<KontrakKerjaResponse>> getDetailKontrakKerja(
            @RequestParam(required = true, value = "kontrak_id") UUID kontrakId)
    {
        var body = new JsonBaseResponse<KontrakKerjaResponse>(
                System.currentTimeMillis(),
                kontrakKerjaService.getDetailKontrak(kontrakId)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/kontrak_kerja/by_nip")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<KontrakKerjaByNipResponse>>> getListKontrakByNip(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<KontrakKerjaByNipResponse>>(
                System.currentTimeMillis(),
                kontrakKerjaService.getListKontrakByNip(nip,page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/kontrak_kerja/stop_kontrak")
    public ResponseEntity<JsonBaseResponse<StopKontrakResponse>> stopKontrak(
            @RequestBody StopKontrakRequest request)
    {
        var body = new JsonBaseResponse<StopKontrakResponse>(
                System.currentTimeMillis(),
                kontrakKerjaService.stopKontrak(request)
        );
        return ResponseEntity.ok(body);
    }
}
