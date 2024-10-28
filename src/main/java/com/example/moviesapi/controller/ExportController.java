package com.example.moviesapi.controller;

import com.example.moviesapi.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    private final ExportService exportService;

    @Autowired
    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @GetMapping(value = "/movies/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> exportMoviesToJson() throws IOException {
        byte[] data = exportService.exportMoviesToJson();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movies.json")
                .body(data);
    }

    @GetMapping(value = "/movies/csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportMoviesToCsv() throws IOException {
        byte[] data = exportService.exportMoviesToCsv();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=movies.csv")
                .body(data);
    }
}