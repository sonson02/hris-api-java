package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.PajakMasterRequest;
import com.example.hrisapi.dto.request.UnitMasterRequest;
import com.example.hrisapi.dto.response.PajakMasterResponse;
import com.example.hrisapi.dto.response.UnitMasterResponse;
import com.example.hrisapi.service.PajakMasterService;
import com.example.hrisapi.service.UnitMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class PajakMasterController {

    @Autowired
    private PajakMasterService pajakMasterService;

    @GetMapping("/pajak")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<PajakMasterResponse>>> getListPajakMaster(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<PajakMasterResponse>>(
                System.currentTimeMillis(),
                pajakMasterService.getListPajakMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/pajak")
    public ResponseEntity<JsonBaseResponse<PajakMasterResponse>> insertPajakMaster(
            @RequestBody PajakMasterRequest request)
    {
        var body = new JsonBaseResponse<PajakMasterResponse>(
                System.currentTimeMillis(),
                pajakMasterService.insertPajakMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/pajak")
    public ResponseEntity<JsonBaseResponse<PajakMasterResponse>> updatePajakMaster(
            @RequestBody PajakMasterRequest request)
    {
        var body = new JsonBaseResponse<PajakMasterResponse>(
                System.currentTimeMillis(),
                pajakMasterService.updatePajakMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
