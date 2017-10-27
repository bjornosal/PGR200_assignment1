package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    

    public DatabaseConnection() throws IOException {
        dataSource = new MysqlDataSource();
    }

    public void databaseBuilder(String propertyFilePath) throws IOException {

        Properties properties = new Properties();
        //This one should be used, using other until dynamic creation is finished
//        InputStream input = new FileInputStream(propertyFilePath);
        InputStream input = new FileInputStream("./src/files/defaultDatabaseConfig.properties");

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
        dataSource.setUser(properties.getProperty("databaseUser"));
        dataSource.setPassword(properties.getProperty("databasePassword"));


    }


    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}
