package com.example.clickhouseingestiontool.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FlatFileConfig  {

    private String fileName;
    private String delimiter;

    public FlatFileConfig() {
    }

    public FlatFileConfig(String fileName, String delimiter) {
        this.fileName = fileName;
        this.delimiter = delimiter;
    }

    @Override
    public String toString() {
        return "FlatFileConfig{" +
                "fileName='" + fileName + '\'' +
                ", delimiter='" + delimiter + '\'' +
                "}";
    }
}
