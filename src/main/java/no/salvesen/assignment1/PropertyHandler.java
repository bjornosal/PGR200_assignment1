package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHandler {


    public void setPropertiesForDatabase(String propertyFilePath, MysqlDataSource dataSource) throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));
    }

    public String getDatabaseName(String propertyFilePath) throws IOException {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        return properties.getProperty("databaseName");
    }
}
