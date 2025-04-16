package com.example.clickhouseingestiontool.controller;

import com.example.clickhouseingestiontool.model.ClickHouseConnectionInfo;
import com.example.clickhouseingestiontool.model.FlatFileConfig;
import com.example.clickhouseingestiontool.model.IngestionResult;
import com.example.clickhouseingestiontool.model.TableSchema;
import com.example.clickhouseingestiontool.service.ClickHouseService;
import com.example.clickhouseingestiontool.service.FlatFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class IngestionController {

    @Autowired
    private ClickHouseService clickHouseService;

    @Autowired
    private FlatFileService flatFileService;

//    @GetMapping("/")
    public String index() {
        return "index"; // Assuming your basic UI is in index.html
    }

    @PostMapping("/clickhouse/connect")
    public ResponseEntity<List<String>> connectClickHouse(@RequestBody ClickHouseConnectionInfo connectionInfo) {
        try {
            List<String> tables = clickHouseService.getClickHouseTables(connectionInfo);
            return ResponseEntity.ok(tables);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of("Connection failed: " + e.getMessage()));
        }
    }

    @PostMapping("/clickhouse/schema")
    public ResponseEntity<List<TableSchema>> getClickHouseTableSchema(@RequestBody ClickHouseConnectionInfo connectionInfo, @RequestParam String tableName) {
        try {
            List<TableSchema> schema = clickHouseService.getClickHouseTableSchema(connectionInfo, tableName);
            return ResponseEntity.ok(schema);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(List.of(new TableSchema("Error", e.getMessage())));
        }
    }

    @PostMapping("/ingest/clickhouse-to-file")
    public ResponseEntity<IngestionResult> ingestClickHouseToFile(@RequestBody ClickHouseConnectionInfo connectionInfo, @RequestParam String tableName, @RequestParam List<String> columns, @RequestBody FlatFileConfig fileConfig) {
        try {
            long recordCount = clickHouseService.ingestToFile(connectionInfo, tableName, columns, fileConfig);
            return ResponseEntity.ok(new IngestionResult("Ingestion successful", recordCount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new IngestionResult("Ingestion failed: " + e.getMessage(), 0));
        }
    }

    @PostMapping("/ingest/file-to-clickhouse")
    public ResponseEntity<IngestionResult> ingestFileToClickHouse(@RequestParam("file") MultipartFile file, @RequestBody ClickHouseConnectionInfo connectionInfo, @RequestParam String tableName, @RequestParam List<String> columns, @RequestBody FlatFileConfig fileConfig) {
        try {
            long recordCount = flatFileService.ingestToClickHouse(file, connectionInfo, tableName, columns, fileConfig);
            return ResponseEntity.ok(new IngestionResult("Ingestion successful", recordCount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new IngestionResult("Ingestion failed: " + e.getMessage(), 0));
        }
    }
}

