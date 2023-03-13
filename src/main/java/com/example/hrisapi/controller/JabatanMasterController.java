package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.response.JabatanMasterResponse;
import com.example.hrisapi.service.JabatanMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class JabatanMasterController {

    @Autowired
    private JabatanMasterService jabatanMasterService;

    @GetMapping("/jabatan")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<JabatanMasterResponse>>> getListJabatanMaster(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<JabatanMasterResponse>>(
                System.currentTimeMillis(),
                jabatanMasterService.getListJabatanMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/jabatan")
    public ResponseEntity<JsonBaseResponse<JabatanMasterResponse>> insertJabatanMaster(
            @RequestBody JabatanMasterRequest request)
    {
        var body = new JsonBaseResponse<JabatanMasterResponse>(
                System.currentTimeMillis(),
                jabatanMasterService.insertJabatanMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/jabatan")
    public ResponseEntity<JsonBaseResponse<JabatanMasterResponse>> updateJabatanMaster(
            @RequestBody JabatanMasterRequest request)
    {
        var body = new JsonBaseResponse<JabatanMasterResponse>(
                System.currentTimeMillis(),
                jabatanMasterService.updateJabatanMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
