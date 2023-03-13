package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.KaryawanRequest;
import com.example.hrisapi.dto.response.KaryawanResponse;
import com.example.hrisapi.service.KaryawanService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class KaryawanController {

    @Autowired
    private KaryawanService karyawanService;

    @GetMapping("/karyawan")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<KaryawanResponse>>> getListKaryawan(
            @RequestParam(required = false) String nip,
            @RequestParam(required = false) String unitName,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<KaryawanResponse>>(
                System.currentTimeMillis(),
                karyawanService.getListKaryawan(nip, unitName, page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/karyawan")
    public ResponseEntity<JsonBaseResponse<KaryawanResponse>> insertKaryawan(
            @RequestBody KaryawanRequest request)
    {
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
}
