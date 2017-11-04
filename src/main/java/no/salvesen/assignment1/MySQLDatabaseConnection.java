package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;


public class MySQLDatabaseConnection implements ConnectionProvider{

    private MysqlDataSource dataSource;
    private PropertyHandler propertyHandler;
    public MySQLDatabaseConnection() {
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
