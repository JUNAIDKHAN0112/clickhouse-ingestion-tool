package com.example.clickhouseingestiontool.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IngestionResult {

    private String status; // "success" or "error"
    private String message;
    private long recordCount;

    public IngestionResult() {
    }

    public IngestionResult(String status, String message, long recordCount) {
        this.status = status;
        this.message = message;
        this.recordCount = recordCount;
    }

    public IngestionResult(String message, long recordCount) {
        this.message = message;
        this.recordCount = recordCount;
    }

    @Override
    public String toString() {
        return "IngestionResult{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", recordCount=" + recordCount +
                "}";
    }
}
