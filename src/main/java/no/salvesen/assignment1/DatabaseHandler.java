package no.salvesen.assignment1;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
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
        //Temporary - prepared statement needs to be implemented with ON DUPLICATE KEY for these to be removed
        dropTable("subject");
        dropTable("room");
        dropTable("lecturer");

        createDatabase();
        createSubjectTable();
        createRoomTable();
        createLecturerTable();
    }


/*    public int findColumnCount(String tableName) throws SQLException {
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
    // Ment to be used to make mostly generic. Resolved using objects instead of 17 if's
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

    public void fillTable(File tableInformation, String tableName) throws SQLException, FileNotFoundException {
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";|\\r\\n");

        MysqlDataSource dataSource = databaseConn.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);

            while (fileStream.hasNext()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < findColumnCount(tableName)+1; i++) {
                    preparedStatement.setObject(i, fileStream.next());
                }
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
     * Should return column numbers, maybe column names to skip those?
     *
     * @param rsmd Takes in ResultSetMetaData to se which column number has an autoincrement, for now only one column.
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

    public String getSubject(String subject) throws SQLException {
        String result = "";
        String query = "SELECT id as 'Kode', name as 'Navn', attending_students as 'Studenter', teaching_form as 'Laeringsform', duration as 'Lengde'\n" +
                "FROM subject\n" +
                "WHERE id = '"+ subject + "';";
        System.out.println("Kode    |   Navn    |   Studenter   |   Laeringsform    |   Lengde");

        MysqlDataSource dataSource = databaseConn.getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                for(int i = 1; i <= findColumnCount("subject"); i++) {
                    if(i <= findColumnCount("subject") && i > 1) {
                        result += "\t|\t";
                    }
                    result += rs.getObject(i);
                }
            }
        }
        return result;
    }

    public String getAllSubjects() throws SQLException {
        String result = "";
        String query = "SELECT id as 'Kode', name as 'Navn', attending_students as 'Studenter', teaching_form as 'Laeringsform', duration as 'Lengde'\n" +
                "FROM subject;";
        System.out.println("\nKode    |   Navn    |   Studenter   |   Laeringsform    |   Lengde\n");
        MysqlDataSource dataSource = databaseConn.getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                for(int i = 1; i <= findColumnCount("subject"); i++) {
                    if(i <= findColumnCount("subject") && i > 1) {
                        result += "\t|\t";
                    }

                    result += rs.getObject(i);

                    if(i == findColumnCount("subject")) {
                        result += "\n";
                    }
                }
            }

        }
        return result;
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


    /**
     * This part is methods that creates all stuff required.
     *
     *
     */
    protected void createTable(String tableName) throws SQLException {
        switch (tableName) {
            case "subject":
                createSubjectTable();
                break;
        }
    }

    //Not in use as of right now
    private boolean checkIfTableExists(String tableName) throws SQLException {
        boolean exists = false;
        try (Connection connection = databaseConn.getDataSource().getConnection()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            System.out.println(rs);
            if(rs.next()) {
                exists = true;
            }
        }
        return exists;
    }

    protected void dropTable(String tableName) throws SQLException {
        try(Connection connection = databaseConn.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS "+tableName);
        }
    }

    private void createDatabase() throws SQLException{
        try (Connection connection = databaseConn.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1;");
        }
    }

    private void createSubjectTable() throws SQLException {
        String tableName = "subject";
        try(Connection connection = databaseConn.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                    "id VARCHAR(255) UNIQUE,\n" +
                    "name varchar(255) UNIQUE NOT NULL,\n" +
                    "attending_students INT(6),\n" +
                    "teaching_form varchar(50) NOT NULL,\n" +
                    // Issue with data truncation might lie here.
                    //Mysql has issues with float, use decimal instead.
                    "duration DECIMAL(11),\n" +
                    //"duration FLOAT(11),\n" +
                    "PRIMARY KEY(id));");
        }
    }

    private void createLecturerTable() throws SQLException {
        try(Connection connection = databaseConn.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS lecturer (\n" +
                    "id int(11) auto_increment,\n" +
                    "name varchar (255),\n" +
                    "PRIMARY KEY (id)\n" +
                    ");");
        }
    }

    private void createRoomTable() throws SQLException {
        try (Connection connection = databaseConn.getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS room (\n" +
                    "name varchar(255) UNIQUE, \n" +
                    "type varchar(255),\n" +
                    //"type ENUM('SMALLROOM', 'LARGEROOM', 'LARGEAUD', 'SMALLAUD'),\n" +
                    "facilities varchar(255)\n" +
                    ");");
        }
    }


}
