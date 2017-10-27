package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private MysqlDataSource dataSource;
    

    public DatabaseConnection() throws SQLException, IOException {
        databaseBuilder();
    }

    public void databaseBuilder() throws SQLException, IOException {
        dataSource = new MysqlDataSource();

        /**
         * Login to webserver.
         * If local server wanted, comment out next four lines and
         * remove comment on next lines. Fill in proper information.
         */ /*
        dataSource.setServerName("tek.westerdals.no");
        dataSource.setDatabaseName("salbjo16_pgr200_assignment1");
        dataSource.setUser("salbjo16_pgr200");
        dataSource.setPassword("pgr200!");
*/
        //Setting up using properties instead

        //Using default login for now.
        Properties properties = new Properties();
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
