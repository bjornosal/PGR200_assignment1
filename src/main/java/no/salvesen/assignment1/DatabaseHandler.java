package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.FileNotFoundException;

import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;


public class DatabaseHandler {

    DatabaseConn databaseConn;

    public DatabaseHandler() throws SQLException {
        databaseConn = new DatabaseConn();
    }
/*
    public int findColumnCount(String tableName) throws SQLException {
        MysqlDataSource dataSource = databaseConn.getDataSource();
        String selectAllQuery = "SELECT * FROM " + tableName + ";";
        int columnCount;

        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllQuery);
            ResultSetMetaData rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
        }
        return columnCount;
    }*/

//Shorter version
    public int findColumnCount(String tableName) throws SQLException {
        int columnCount = getFullResultSetMetaData(tableName).getColumnCount();
        return columnCount;
    }

    public int getRowCount(String tableName) throws SQLException {
        MysqlDataSource dataSource = databaseConn.getDataSource();
        String selectAllQuery = "SELECT * FROM " + tableName + ";";
        int rowCount = 0;

        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(selectAllQuery);

            while (rs.next()) {
                rowCount++;
            }
        }
        return rowCount;
    }

    //MetaData can be used for finding all column names as well - method to parse through column names maybe?
    private String[] getColumnNames(String tableName) throws SQLException {
        String[] columnNames = new String[findColumnCount(tableName)];
        String query = "SELECT * FROM " + tableName + ";";
        //Shorten code by taking entire try - with resource out of methods??
        MysqlDataSource dataSource = databaseConn.getDataSource();

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

        MysqlDataSource dataSource = databaseConn.getDataSource();
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

    public void fillTable(File tableInformation, String tableName, String filePath) throws SQLException, FileNotFoundException {
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";|\\r\\n");
        String[] dataTypes = getDataTypes(tableName);

        MysqlDataSource dataSource = databaseConn.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);

            while (fileStream.hasNext()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < findColumnCount(tableName)+1; i++) {
                    preparedStatement.setObject(i, fileStream.next());
                }
                System.out.println(preparedStatement.toString());
                preparedStatement.executeUpdate();

            }
        }
    }

    private String prepareInsertStatement(String tableName) throws SQLException {
        String prpStatement = "INSERT INTO " + tableName + " VALUES (";
        int columns = findColumnCount(tableName);
        for(int i = 1; i < columns; i++) {
            prpStatement += "?, ";
            if (i == columns - 1) {
                prpStatement += "?);";
            }
        }
        return prpStatement;
    }

    /***
     * Implement usage for more generic methods
     * @param rsmd
     * @return int
     * @throws SQLException
     */
    private int findAutoIncrement(ResultSetMetaData rsmd) throws SQLException {
        int columnNumber = -1;

        for(int i = 1; i < rsmd.getColumnCount(); i ++) {
            if(rsmd.isAutoIncrement(i)) {
                columnNumber = i;
            }
        }

        return columnNumber;
    }


    private ResultSetMetaData getFullResultSetMetaData(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData rsmd;

        MysqlDataSource dataSource = databaseConn.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rsmd = rs.getMetaData();
        }
        return rsmd;
    }

}
