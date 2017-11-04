package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLDatabaseConnection implements ConnectionProvider{

    private MysqlDataSource dataSource;
    public MySQLDatabaseConnection() {
        dataSource = new MysqlDataSource();
    }

    @Override
    public void setPropertiesForDatabase(String propertyFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
