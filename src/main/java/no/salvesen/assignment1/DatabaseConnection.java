package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    private PropertiesHandler propertiesHandler;

    /**
     * Instantiates a new Database connection.
     *
     * @param propertiesHandler the properties handler
     */
    public DatabaseConnection(PropertiesHandler propertiesHandler) {
        dataSource = new MysqlDataSource();
        this.propertiesHandler = propertiesHandler;
    }

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Initialize properties.
     *
     * @throws IOException the io exception
     */
    public void initializeProperties() throws IOException {
        propertiesHandler.assignPropertiesToDatasource(getDataSource());
    }

    /**
     * Sets database name for datasource.
     *
     * @throws IOException the io exception
     */
    public void setDatabaseNameForDatasource() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertiesHandler.getPropertyFilePath());
        properties.load(input);

        dataSource.setDatabaseName(properties.getProperty("databaseName"));
    }

    private MysqlDataSource getDataSource() {
        return dataSource;
    }
}