package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;


public class DatabaseConnection {

    private MysqlDataSource dataSource;
    private PropertyHandler propertyHandler;

    public DatabaseConnection() {
        dataSource = new MysqlDataSource();
        propertyHandler = new PropertyHandler();
    }

    public void setPropertiesForDatabase(String propertyFilePath) throws IOException {
        propertyHandler.setPropertiesForDatabase(propertyFilePath, dataSource);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
