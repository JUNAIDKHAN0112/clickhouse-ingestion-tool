package com.example.clickhouseingestiontool.service;

import com.example.clickhouseingestiontool.model.ClickHouseConnectionInfo;
import com.example.clickhouseingestiontool.model.FlatFileConfig;
import com.example.clickhouseingestiontool.model.TableSchema;
import org.springframework.stereotype.Service;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ClickHouseService {

    @Value("${clickhouse.host}")
    private String host;

    @Value("${clickhouse.port}")
    private int port;

    @Value("${clickhouse.database}")
    private String database;

    @Value("${clickhouse.user}")
    private String user;

    public List<String> getClickHouseTables(ClickHouseConnectionInfo connectionInfo) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", connectionInfo.getUser());
        properties.setProperty("password", connectionInfo.getJwtToken()); // Assuming password field for JWT

        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                String.format("jdbc:clickhouse://%s:%d/%s", connectionInfo.getHost(), connectionInfo.getPort(), connectionInfo.getDatabase()),
                properties
        );

        List<String> tableNames = new ArrayList<>();
        try (ClickHouseConnection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString(1));
            }
        }
        return tableNames;
    }

    public List<TableSchema> getClickHouseTableSchema(ClickHouseConnectionInfo connectionInfo, String tableName) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", connectionInfo.getUser());
        properties.setProperty("password", connectionInfo.getJwtToken());

        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                String.format("jdbc:clickhouse://%s:%d/%s", connectionInfo.getHost(), connectionInfo.getPort(), connectionInfo.getDatabase()),
                properties
        );

        List<TableSchema> schema = new ArrayList<>();
        try (ClickHouseConnection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(String.format("DESCRIBE %s", tableName))) {
            while (resultSet.next()) {
                schema.add(new TableSchema(resultSet.getString("name"), resultSet.getString("type")));
            }
        }
        return schema;
    }

    public long ingestToFile(ClickHouseConnectionInfo connectionInfo, String tableName, List<String> columns, FlatFileConfig fileConfig) throws Exception {
        // Implement logic to query ClickHouse and write to a flat file
        // Use JDBC to query, and Java IO to write. Handle batching if needed.
        StringBuilder selectQuery = new StringBuilder("SELECT ");
        selectQuery.append(String.join(",", columns)).append(" FROM ").append(tableName);

        Properties properties = new Properties();
        properties.setProperty("user", connectionInfo.getUser());
        properties.setProperty("password", connectionInfo.getJwtToken());

        ClickHouseDataSource dataSource = new ClickHouseDataSource(
                String.format("jdbc:clickhouse://%s:%d/%s", connectionInfo.getHost(), connectionInfo.getPort(), connectionInfo.getDatabase()),
                properties
        );

        long recordCount = 0;
        try (ClickHouseConnection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery.toString());
             java.io.FileWriter writer = new java.io.FileWriter(fileConfig.getFileName())) {

            // Write header row
            writer.write(String.join(fileConfig.getDelimiter(), columns));
            writer.write(System.lineSeparator());

            while (resultSet.next()) {
                List<String> rowData = new ArrayList<>();
                for (String column : columns) {
                    rowData.add(resultSet.getString(column));
                }
                writer.write(String.join(fileConfig.getDelimiter(), rowData));
                writer.write(System.lineSeparator());
                recordCount++;
            }
        }
        return recordCount;
    }
}

