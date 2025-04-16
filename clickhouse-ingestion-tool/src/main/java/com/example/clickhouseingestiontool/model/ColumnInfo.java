
package com.example.clickhouseingestiontool.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ColumnInfo {

    private String columnName;
    private String dataType;

    public ColumnInfo() {
    }

    public ColumnInfo(String columnName, String dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                "}";
    }
}
