package com.example.clickhouseingestiontool.controller;

import com.example.clickhouseingestiontool.model.ClickHouseConnectionInfo;
import com.example.clickhouseingestiontool.service.ClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.List;

@RestController
public class ClickHouseController {

    @Autowired
    private ClickHouseService clickHouseService;

//    @GetMapping("/clickhouse/tables")
    public List<String> getTables() throws SQLException {
        //  In a real application, you would get this from a request, not hardcoded.
        ClickHouseConnectionInfo connectionInfo = new ClickHouseConnectionInfo();
        connectionInfo.setHost("localhost");  // Or from application.properties
        connectionInfo.setPort(8123);        // Or 9000
        connectionInfo.setDatabase("default");  // Or your database
        connectionInfo.setUser("default");      // Or your user
        connectionInfo.setJwtToken("");  //  Set this if you are using JWT
        return clickHouseService.getClickHouseTables(connectionInfo);
    }
}