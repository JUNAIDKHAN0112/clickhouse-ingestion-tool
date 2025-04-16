# ClickHouse Ingestion Tool

## Overview

The ClickHouse Ingestion Tool is a web application designed to facilitate data transfer between ClickHouse and flat files (like CSV). It provides a user-friendly interface to configure connections, select data sources, and manage the ingestion process. This tool is built with Spring Boot (Java) for the backend and React for the frontend.

## Features

* Bidirectional Data Flow: Supports ingestion from ClickHouse to flat files and from flat files to ClickHouse.
* Data Source Selection: Users can choose between ClickHouse and Flat File as the data source.
* ClickHouse Connection Configuration:
    * UI for Host, Port, Database, User, and JWT Token.
    * Authentication using JWT tokens.
    * Uses the official ClickHouse JDBC driver.
* Flat File Integration:
    * UI configuration for local flat file name and delimiters.
    * Uses standard Java IO libraries for file handling.
* Schema Discovery and Column Selection:
    * Fetches table schemas from ClickHouse and flat file structures.
    * Displays column names with checkboxes for selection.
* Data Ingestion:
    * Efficient data transfer using batching.
    * Reports the total count of ingested records upon success.
* Error Handling:
    * Handles connection, authentication, query, and ingestion errors.
    * Displays user-friendly error messages in the UI.

## Technical Details

* Backend: Java (Spring Boot)
* Frontend: HTML/CSS/JS
* ClickHouse:
    * Tested with a local ClickHouse instance.
    * Uses the ClickHouse JDBC driver.
* Build Tool: Maven
* Testing: JUnit 5, Spring Boot Test

 


## Setup and Installation

1.  Prerequisites:
    * Java Development Kit (JDK) 20
    * Maven
    * Git
    * A running ClickHouse instance (local or cloud)
2.  Clone the repository:

    ```
    git clone [(https://github.com/JUNAIDKHAN0112/clickhouse-ingestion-tool.git)]
    cd clickhouse-ingestion-tool
    ```
3.  Configure ClickHouse connection:
    * Create a file `src/main/resources/application.properties`
    * Add your ClickHouse connection details:

        ```
        clickhouse.host=your_clickhouse_host
        clickhouse.port=8123 # Or 9000
        clickhouse.database=default
        clickhouse.user=default
        #clickhouse.jwtToken=YOUR_TOKEN
        ```
4.  Build the application:

    ```
    mvn clean install
    ```
5.  Run the application:

    ```
    java -jar target/clickhouse-ingestion-tool-0.0.1-SNAPSHOT.jar
    ```
6.  Access the UI:
    * Open your web browser and go to `http://localhost:8080`

## Testing

The following test cases were implemented:

1.  ClickHouse to Flat File:
    * Ingest data from a single ClickHouse table to a flat file.
    * Verified the record count in the generated file.
2.  Flat File to ClickHouse:
    * Ingest data from a flat file to a new ClickHouse table.
    * Verified the record count and data integrity in the ClickHouse table.
3.  Connection/Authentication Failures:
    * Tested handling of invalid ClickHouse connection parameters and JWT tokens.

## AI Tools Usage

I used AI tools (e.g., ChatGPT) during the development process. Here are some of the prompts I utilized:

* "Generate a Spring Boot application to connect to ClickHouse"
* "Create a React component for ClickHouse connection configuration"
* "Implement ClickHouse to file ingestion in Java"
* "Write a function to parse a CSV file in Java"
* "Fix the 'Whitelabel Error Page' in Spring Boot"
* "Help me to resolve the dependency issue in maven"
* "Write a good README.md file"

## Future Enhancements

* Implement Multi-Table Joins for ClickHouse source.
* Add a progress bar to the UI during ingestion.
* Implement a data preview feature to display the first 100 records.
* Add unit tests for the backend logic.
* Implement user authentication.
