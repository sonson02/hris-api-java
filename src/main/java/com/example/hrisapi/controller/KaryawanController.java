package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedDashboardResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.request.StopKaryawanRequest;
import com.example.hrisapi.dto.request.StopKontrakRequest;
import com.example.hrisapi.dto.response.*;
import com.example.hrisapi.service.KaryawanService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    @GetMapping("/karyawan")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<ListKaryawanResponse>>> getListKaryawan(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, value = "karyawan_name") String name,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<ListKaryawanResponse>>(
                System.currentTimeMillis(),
                karyawanService.getListKaryawan(nip, name, unitId, page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/karyawan")
    public ResponseEntity<JsonBaseResponse<KaryawanResponse>> insertKaryawan(
            @RequestBody KaryawanRequest request) throws ParseException {
        var body = new JsonBaseResponse<KaryawanResponse>(
                System.currentTimeMillis(),
                karyawanService.insertKaryawan(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/karyawan")
    public ResponseEntity<JsonBaseResponse<KaryawanResponse>> updateKaryawan(
            @RequestBody KaryawanRequest request)
    {
        var body = new JsonBaseResponse<KaryawanResponse>(
                System.currentTimeMillis(),
                karyawanService.updateKaryawan(request)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/karyawan/detail")
    public ResponseEntity<JsonBaseResponse<KaryawanResponse>> getDetailKaryawan(
            @RequestParam(required = true, value = "karyawan_id") UUID karyawanId)
    {
        var body = new JsonBaseResponse<KaryawanResponse>(
                System.currentTimeMillis(),
                karyawanService.getDetailKaryawan(karyawanId)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/karyawan/by_nip")
    public ResponseEntity<JsonBaseResponse<KaryawanByNipResponse>> getKaryawanByNip(
            @RequestParam(required = true, value = "nip") String karyawanNip)
    {
        var body = new JsonBaseResponse<KaryawanByNipResponse>(
                System.currentTimeMillis(),
                karyawanService.getKaryawanByNip(karyawanNip)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/karyawan/dashboard")
    public ResponseEntity<JsonBaseResponse<PaginatedDashboardResponse<ListKaryawanResponse>>> getKaryawanDashboard(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer days)
    {
        var body = new JsonBaseResponse<PaginatedDashboardResponse<ListKaryawanResponse>>(
                System.currentTimeMillis(),
                karyawanService.getKaryawanDashboard(page, size, days)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/karyawan/stop_karyawan")
    public ResponseEntity<JsonBaseResponse<StopKaryawanResponse>> stopKaryawan(
            @RequestBody StopKaryawanRequest request)
    {
        var body = new JsonBaseResponse<StopKaryawanResponse>(
                System.currentTimeMillis(),
                karyawanService.stopKaryawan(request)
        );
        return ResponseEntity.ok(body);
    }
}
