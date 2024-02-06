package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.BankMasterRequest;
import com.example.hrisapi.dto.response.BankMasterResponse;
import com.example.hrisapi.service.BankMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class BankMasterController {

    @Autowired
    private BankMasterService bankMasterService;

    @GetMapping("/bank")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<BankMasterResponse>>> getListBankMaster(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "1000") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<BankMasterResponse>>(
                System.currentTimeMillis(),
                bankMasterService.getListBankMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/bank")
    public ResponseEntity<JsonBaseResponse<BankMasterResponse>> insertBankMaster(
            @RequestBody BankMasterRequest request)
    {
        var body = new JsonBaseResponse<BankMasterResponse>(
                System.currentTimeMillis(),
                bankMasterService.insertBankMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/bank")
    public ResponseEntity<JsonBaseResponse<BankMasterResponse>> updateBankMaster(
            @RequestBody BankMasterRequest request)
    {
        var body = new JsonBaseResponse<BankMasterResponse>(
                System.currentTimeMillis(),
                bankMasterService.updateBankMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
