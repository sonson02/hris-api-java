package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.JabatanMasterRequest;
import com.example.hrisapi.dto.request.UnitMasterRequest;
import com.example.hrisapi.dto.response.JabatanMasterResponse;
import com.example.hrisapi.dto.response.UnitMasterResponse;
import com.example.hrisapi.service.JabatanMasterService;
import com.example.hrisapi.service.UnitMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class UnitMasterController {

    @Autowired
    private UnitMasterService unitMasterService;

    @GetMapping("/unit")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<UnitMasterResponse>>> getListUnitMaster(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<UnitMasterResponse>>(
                System.currentTimeMillis(),
                unitMasterService.getListUnitMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/unit")
    public ResponseEntity<JsonBaseResponse<UnitMasterResponse>> insertUnitMaster(
            @RequestBody UnitMasterRequest request)
    {
        var body = new JsonBaseResponse<UnitMasterResponse>(
                System.currentTimeMillis(),
                unitMasterService.insertUnitMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/unit")
    public ResponseEntity<JsonBaseResponse<UnitMasterResponse>> updateUnitMaster(
            @RequestBody UnitMasterRequest request)
    {
        var body = new JsonBaseResponse<UnitMasterResponse>(
                System.currentTimeMillis(),
                unitMasterService.updateUnitMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
