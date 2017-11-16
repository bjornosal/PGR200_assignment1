package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    private PropertiesHandler propertiesHandler;

    public DatabaseConnection(PropertiesHandler propertiesHandler) {
        dataSource = new MysqlDataSource();
        this.propertiesHandler = propertiesHandler;
    }

    /**
     * Sets up the properties for the datasource.
     *
     * @throws IOException If no file is found or unable to load the InputStream.
     */
    public void initializeProperties() throws IOException {
        propertiesHandler.initializeProperties(getDataSource());
    }


    /**
     * Returns a connection to the database.
     * @return A MysqlDataSource Connection.
     * @throws SQLException If unable to get a connection.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void setDataSourceDatabaseName() throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertiesHandler.getPropertyFilePath());
        properties.load(input);

        dataSource.setDatabaseName(properties.getProperty("databaseName"));
    }

    private MysqlDataSource getDataSource() {
        return dataSource;
    }
}