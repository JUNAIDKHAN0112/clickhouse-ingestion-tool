package com.example.clickhouseingestiontool.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TableSchema {

    private String tableName;
    private String columnName; // Changed from List<String> to a single columnName
    private String columnType;

    public TableSchema() {
    }

    public TableSchema(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public TableSchema(String tableName, String columnName, String columnType) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnType = columnType;
    }

    @Override
    public String toString() {
        return "TableSchema{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                "}";
    }
}