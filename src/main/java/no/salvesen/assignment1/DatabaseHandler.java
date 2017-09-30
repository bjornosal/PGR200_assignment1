package no.salvesen.assignment1;

import com.mysql.jdbc.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.mysql.jdbc.jdbc2.optional.PreparedStatementWrapper;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;
import java.sql.Types;

public class DatabaseHandler {

    //Wanted - Generic method, that takes in a file, and parses that file to fill designated table.
    //Check what fields are required - read metadata?
    //Parse the file for that amount of fields
    //Use scanner to parse file depending on amount of fields
    DatabaseConnector databaseConnector = new DatabaseConnector();

//Check if table exists

    public int findColumnCount(String tableName) throws SQLException {
        MysqlDataSource dataSource = databaseConnector.getDataSource();
        String query = "SELECT * FROM " + tableName + ";";
        System.out.println(query);
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            return rsmd.getColumnCount() - 1;
        }
    }

    //MetaData can be used for finding all column names as well - method to parse through column names maybe?
    private String[] getColumnNames(String tableName) throws SQLException {
        String[] columnNames = new String[findColumnCount(tableName)];
        String query = "SELECT * FROM " + tableName + ";";
        //Shorten code by taking entire try - with resource out of methods??
        MysqlDataSource dataSource = databaseConnector.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < findColumnCount(tableName); i++) {
                columnNames[i-1] = rsmd.getColumnName(i);
            }
        }

        return columnNames;
    }

    //SOURCE: https://stackoverflow.com/questions/12367828/how-can-i-get-different-datatypes-from-resultsetmetadata-in-java
    private String[] getDataTypes(String tableName) throws SQLException {
        String[] dataTypes = new String[findColumnCount(tableName)];
        String query = "SELECT * FROM " + tableName + ";";

        MysqlDataSource dataSource = databaseConnector.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();

            for(int i = 1; i < findColumnCount(tableName); i++) {
                String dataType = rsmd.getColumnTypeName(i);
                dataTypes[i-1] = dataType;
            }
        }

        return dataTypes;
    }

    //What does file name print out?
    public void fillTable(File tableInformation, String tableName, String filePath) throws SQLException, FileNotFoundException {
        //Needed - Need to loop through file, and run query for each line found, stop at the column count
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";");
        String[] dataTypes = getDataTypes(tableName);

        MysqlDataSource dataSource = databaseConnector.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);
            //Missing information - what kind of datatype is in each field
            //Correct way to iterate over??
            while (fileStream.hasNextLine()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < findColumnCount(tableName); i++) {
                    //Using setObject instead of running 17 if tests to use correct type
                    preparedStatement.setObject(i, fileStream.next());
                }

            }
        }
    }

    private String prepareInsertStatement(String tableName) throws SQLException {
        String prpStatement = "INSERT INTO ? VALUES (";
        int columns = findColumnCount(tableName);
        for(int i = 1; i < columns; i++) {
            prpStatement += "?, ";
            if (i == columns - 1) {
                prpStatement += "?);";
            }
        }
        return prpStatement;
    }


}
