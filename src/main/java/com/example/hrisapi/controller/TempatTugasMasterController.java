package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.request.TempatTugasMasterRequest;
import com.example.hrisapi.dto.response.TempatTugasMasterResponse;
import com.example.hrisapi.service.TempatTugasMasterService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/master")
public class TempatTugasMasterController {

    @Autowired
    private TempatTugasMasterService tempatTugasMasterService;

    @GetMapping("/tempat_tugas")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<TempatTugasMasterResponse>>> getListTempatTugasMaster(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "1000") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<TempatTugasMasterResponse>>(
                System.currentTimeMillis(),
                tempatTugasMasterService.getListTempatTugasMaster(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @PostMapping("/tempat_tugas")
    public ResponseEntity<JsonBaseResponse<TempatTugasMasterResponse>> insertTempatTugasMaster(
            @RequestBody TempatTugasMasterRequest request)
    {
        var body = new JsonBaseResponse<TempatTugasMasterResponse>(
                System.currentTimeMillis(),
                tempatTugasMasterService.insertTempatTugasMaster(request)
        );
        return ResponseEntity.ok(body);
    }

    @PutMapping("/tempat_tugas")
    public ResponseEntity<JsonBaseResponse<TempatTugasMasterResponse>> updateTempatTugasMaster(
            @RequestBody TempatTugasMasterRequest request)
    {
        var body = new JsonBaseResponse<TempatTugasMasterResponse>(
                System.currentTimeMillis(),
                tempatTugasMasterService.updateTempatTugasMaster(request)
        );
        return ResponseEntity.ok(body);
    }
}
