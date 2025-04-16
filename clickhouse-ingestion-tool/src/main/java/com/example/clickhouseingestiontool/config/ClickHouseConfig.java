package com.example.clickhouseingestiontool.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class ClickHouseConfig {

    @Value("${clickhouse.host}")
    private String host;

    @Value("${clickhouse.port}")
    private int port;

    @Value("${clickhouse.database}")
    private String database;

    @Value("${clickhouse.user}")
    private String user;

    // Consider if you want to manage the DataSource as a Spring Bean
    // For a quick implementation, you might instantiate it directly in the service.
    // @Bean
    // public DataSource clickHouseDataSource() {
    //     Properties properties = new Properties();
    //     properties.setProperty("user", user);
    //     // How you pass the JWT token might depend on the ClickHouse JDBC driver
    //     // Example (may need adjustment):
    //     // properties.setProperty("password", jwtToken);
    //     return new ClickHouseDataSource(String.format("jdbc:clickhouse://%s:%d/%s", host, port, database), properties);
    // }
}