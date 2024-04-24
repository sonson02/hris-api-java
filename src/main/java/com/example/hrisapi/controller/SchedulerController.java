package com.example.hrisapi.controller;

import com.example.hrisapi.api.base.JsonBaseResponse;
import com.example.hrisapi.api.base.PaginatedResponse;
import com.example.hrisapi.dto.response.HistoryGajiResponse;
import com.example.hrisapi.dto.response.KontrakKerjaTglHabisKontrakResponse;
import com.example.hrisapi.service.SchedulerService;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class SchedulerController {

    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/scheduler-habis-kontrak")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<KontrakKerjaTglHabisKontrakResponse>>> schedulerTglHbsKontrak(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "1000") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<KontrakKerjaTglHabisKontrakResponse>>(
                System.currentTimeMillis(),
                schedulerService.schedulerTglHabisKontrak(page, size)
        );
        return ResponseEntity.ok(body);
    }

    @GetMapping("/scheduler-history-gaji")
    public ResponseEntity<JsonBaseResponse<PaginatedResponse<HistoryGajiResponse>>> schedulerHistoryGaji(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "1000") Integer size)
    {
        var body = new JsonBaseResponse<PaginatedResponse<HistoryGajiResponse>>(
                System.currentTimeMillis(),
                schedulerService.schedulerHistoryGaji(page, size)
        );
        return ResponseEntity.ok(body);
    }

}
