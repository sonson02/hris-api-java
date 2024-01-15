package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.response.SlipGajiResponse;
import com.example.hrisapi.service.SlipGajiService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SlipGajiController {

    @Autowired
    private SlipGajiService slipGajiService;

    @GetMapping("/slip_gaji")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<SlipGajiResponse>>> getListSlipGaji(
            @RequestParam(required = false, value = "nip") String nip,
            @RequestParam(required = false, value = "karyawan_name") String name,
            @RequestParam(required = false, value = "unit_id") UUID unitId,
            @RequestParam(required = false, value = "periode") String periode,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<SlipGajiResponse>>(
                System.currentTimeMillis(),
                slipGajiService.getListSlipGaji(nip, name, unitId, periode, page, size)
        );
        return ResponseEntity.ok(body);
    }
}
