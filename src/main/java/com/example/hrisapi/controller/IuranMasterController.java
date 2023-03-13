package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.IuranMasterRequest;
import com.example.hrisapi.dto.response.IuranMasterResponse;
import com.example.hrisapi.service.IuranMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class IuranMasterController {

    @Autowired
    private IuranMasterService iuranMasterService;

    @GetMapping("/iuran")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<IuranMasterResponse>>> getListIuranMaster(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<IuranMasterResponse>>(
                System.currentTimeMillis(),
                iuranMasterService.getListIuranMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/iuran")
    public ResponseEntity<JsonBaseResponse<IuranMasterResponse>> insertIuranMaster(
            @RequestBody IuranMasterRequest request)
    {
        var body = new JsonBaseResponse<IuranMasterResponse>(
                System.currentTimeMillis(),
                iuranMasterService.insertIuranMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/iuran")
    public ResponseEntity<JsonBaseResponse<IuranMasterResponse>> updateIuranMaster(
            @RequestBody IuranMasterRequest request)
    {
        var body = new JsonBaseResponse<IuranMasterResponse>(
                System.currentTimeMillis(),
                iuranMasterService.updateIuranMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
