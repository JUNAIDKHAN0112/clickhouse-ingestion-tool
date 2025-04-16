package com.example.clickhouseingestiontool.service;

import com.example.clickhouseingestiontool.model.ClickHouseConnectionInfo;
import com.example.clickhouseingestiontool.model.FlatFileConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class FlatFileService {

    public long ingestToClickHouse(MultipartFile file, ClickHouseConnectionInfo connectionInfo, String tableName, List<String> columns, FlatFileConfig fileConfig) throws Exception {
        long recordCount = 0;
        Properties properties = new Properties();
        properties.setProperty("user", connectionInfo.getUser());
        properties.setProperty("password", connectionInfo.getJwtToken());

        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                String.format("jdbc:clickhouse://%s:%d/%s", connectionInfo.getHost(), connectionInfo.getPort(), connectionInfo.getDatabase()),
                properties
        );

        try (ClickHouseConnection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String headerLine = reader.readLine(); // Read header
            if (headerLine == null) {
                throw new Exception("Empty file");
            }
            List<String> fileColumns = Arrays.asList(headerLine.split(fileConfig.getDelimiter()));
            // Basic validation: check if selected columns exist in the file
            if (!columns.stream().allMatch(fileColumns::contains)) {
                throw new Exception("Selected columns not found in the file header");
            }

            String createTableStatement = String.format("CREATE TABLE IF NOT EXISTS %s (%s) ENGINE = MergeTree() ORDER BY tuple()",
                    tableName,
                    columns.stream().map(col -> String.format("%s String", col)).collect(Collectors.joining(", ")) // Simple String type for all
            );
            statement.execute(createTableStatement);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(fileConfig.getDelimiter());
                // Need to map file columns to the selected ClickHouse columns
                StringBuilder insertStatement = new StringBuilder(String.format("INSERT INTO %s (%s) VALUES (", tableName, String.join(",", columns)));
                List<String> rowValues = columns.stream()
                        .map(col -> {
                            int index = fileColumns.indexOf(col);
                            return (index != -1 && index < values.length) ? "'" + values[index].replace("'", "\\'") + "'" : "''"; // Basic quoting
                        })
                        .collect(Collectors.toList());
                insertStatement.append(String.join(",", rowValues)).append(")");
                statement.execute(insertStatement.toString());
                recordCount++;
            }
        }
        return recordCount;
    }
}