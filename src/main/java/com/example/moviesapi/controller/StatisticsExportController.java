package com.example.moviesapi.controller;

import com.example.moviesapi.service.StatisticsExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/statistics/export")
public class StatisticsExportController {

    private final StatisticsExportService exportService;

    @Autowired
    public StatisticsExportController(StatisticsExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping(value = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> exportToJson() throws IOException {
        byte[] data = exportService.exportToJson();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=movie-statistics.json")
                .body(data);
    }

    @GetMapping(value = "/csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportToCsv() throws IOException {
        byte[] data = exportService.exportToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=movie-statistics.csv")
                .body(data);
    }
}