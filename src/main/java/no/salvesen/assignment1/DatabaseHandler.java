package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.sql.*;

public class DatabaseHandler {

    //Wanted - Generic method, that takes in a file, and parses that file to fill designated table.
    //Check what fields are required - read metadata?
    //Parse the file for that amount of fields
    //Use scanner to parse file depending on amount of fields
    DatabaseConnector databaseConnector = new DatabaseConnector();
    // use resultsetmetadata to get column count

    private int findColumnCount(String tableName) throws SQLException {
        MysqlDataSource dataSource = databaseConnector.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM" + tableName + ";");
            ResultSetMetaData rsmd = rs.getMetaData();
            return rsmd.getColumnCount();
        }
    }
    //MetaData can be used for finding all column names aswell - method to parse through column names maybe?

    public void fillTable(File tableInformation) {

    }


}
