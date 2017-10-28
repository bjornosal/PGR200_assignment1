package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection;
    private FileReader fileReader;
    private String propertyFilePath;

    protected DatabaseHandler() throws IOException {
        databaseConnection = new DatabaseConnection();
        fileReader  = new FileReader();
    }


    private String getResultHeader(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String[] columnDisplayNames = new String[fileReader.getTableColumnCount()];
        for (int i = 0; i < columnDisplayNames.length; i++) {
            columnDisplayNames[i] = fileReader.getDisplayNames().get(i);
        }
        return String.format(getResultFormat(), columnDisplayNames);

    }

    public void tearDownDatabaseAndSetBackUp(File subjectFile, File roomFile, File lecturerFile) throws SQLException, FileNotFoundException {
        dropTable("subject");
        dropTable("room");
        dropTable("lecturer");

        createDatabase();
//        createTableWithTableName("subject");
        createTableFromMetaData("subject");
        createTableFromMetaData("room");
        createTableFromMetaData("lecturer");
        fillTableFromFileByTableName("subject");
        fillTableFromFileByTableName("room");
        fillTableFromFileByTableName("lecturer");

 /*       createTableWithTableName("room");
        createTableWithTableName("lecturer");
        fillTable(subjectFile, "subject");
        fillTable(roomFile, "room");
        fillTable(lecturerFile, "lecturer");
 */   }



    /**
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    private int getColumnCountOfTable(String tableName) throws SQLException {
        return getResultSetMetaDataForEntireTable(tableName).getColumnCount();
    }

    /**
     *
     * @param tableInformation
     * @param tableName
     * @throws SQLException
     * @throws FileNotFoundException
     */

    private void fillTable(File tableInformation, String tableName) throws SQLException, FileNotFoundException {
        Scanner fileStream = new Scanner(tableInformation);
        fileStream.useDelimiter(";|\\r\\n|\\n");

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String prpStmt = prepareInsertStatement(tableName);

            while (fileStream.hasNext()) {
                PreparedStatement preparedStatement = connection.prepareStatement(prpStmt);
                for(int i = 1; i < getColumnCountOfTable(tableName)+1; i++) {
                    preparedStatement.setObject(i, fileStream.next());
                }
                preparedStatement.executeUpdate();
            }
        }
    }

    /**
     * Puts together a prepared statement based on a table name.
     *
     * @param tableName
     * @return a prepared query
     * @throws SQLException
     */

    //TODO do a batch insertion instead, to decrease runtime
    private String prepareInsertStatement(String tableName) throws SQLException {
        String preparedStatement = "INSERT INTO " + tableName + " VALUES (";
        int columns = getColumnCountOfTable(tableName);
        for(int i = 1; i < columns; i++) {
            preparedStatement += "?, ";
            if (i == columns - 1) {
                preparedStatement += "?);";
            }
        }
        return preparedStatement;
    }

    private String prepareInsertStatementBasedOnMetaData(String tableName) throws FileNotFoundException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));

        StringBuilder preparedStatement = new StringBuilder();
        preparedStatement.append("INSERT INTO ").append(fileReader.getTableName()).append("\nVALUES (");

        for(int i = 1; i < fileReader.getTableColumnCount(); i++) {
            preparedStatement.append("?, ");
            if(i == fileReader.getTableColumnCount() - 1) {
                preparedStatement.append("?);");
            }
        }

        return preparedStatement.toString();
    }

    private void fillTableFromFileByTableName(String tableName) throws FileNotFoundException, SQLException {
        File tableFile = fileReader.getFileByTableName(tableName);
        fileReader.readFile(tableFile);

        ArrayList<String> insertionValues = fileReader.getInsertionValues();

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            String preparedInsert = prepareInsertStatementBasedOnMetaData(tableName);
            PreparedStatement preparedStatement = connection.prepareStatement(preparedInsert);


            //TODO add method for parsing through insertionValues and creating a new batch on each getColumnCount
            /*for(int i = 1; i < insertionValues.size(); i++)  {
                preparedStatement.setObject(i, insertionValues.get(i));
            }*/
         /*   int index = 1;
            int counter = 0;
            while(counter < insertionValues.size()) {
                while (index < fileReader.getTableColumnCount()) {
                    preparedStatement.setObject(index, insertionValues.get(index-1));
                    if(index == fileReader.getTableColumnCount()) {
                        System.out.println(preparedStatement.toString());
                        preparedStatement.addBatch();
                    }
                    index++;
                    counter++;
                }
                preparedStatement.executeBatch();
                index = 1;
            }*/
        }
    }


    /***
     * Implement usage for more generic methods
     * Should return column numbers, maybe column names to skip those?
     *
     * @param resultSetMetaData Takes in ResultSetMetaData to see which column number has an autoincrement, for now only one column.
     * @return int
     * @throws SQLException
     */
    private int findAutoIncrement(ResultSetMetaData resultSetMetaData) throws SQLException {
        int columnNumber = -1;

        for(int i = 1; i < resultSetMetaData.getColumnCount(); i ++) {
            if(resultSetMetaData.isAutoIncrement(i)) {
                columnNumber = i;
            }
        }

        return columnNumber;
    }

    //TODO Create a dynamic search and a search for all based on display names

    public String getRowsFromTableByColumnNameAndSearchColumnValue(String tableName, String columnName, String columnValue) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String result = "";
        String query =  buildSelectQuery(true, tableName, columnName);

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, columnValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            result += resultStringBuilder(resultSet);
        }
        return result;
    }

    private String buildSelectQuery(boolean isSpecifiedSearch, String tableName, String columnName) {
        StringBuilder searchQuery = new StringBuilder();
        searchQuery.append("SELECT ");
        for(int i = 0; i < fileReader.getTableColumnCount(); i++)
        {
            searchQuery.append(fileReader.getColumnNames().get(i));
            if(i < fileReader.getTableColumnCount() - 1) {
                searchQuery.append(", ");
            }
        }
        searchQuery.append("\n").append("FROM ").append(tableName);
        if(isSpecifiedSearch) {
            searchQuery.append("\n").append("WHERE ").append(columnName).append(" = ?");
        }
        searchQuery.append(";");

        return searchQuery.toString();
    }

    private String resultStringBuilder(ResultSet resultSet) throws SQLException, FileNotFoundException {
        String[] rowResult = new String[getColumnCountOfTable(fileReader.getTableName())];
        StringBuilder result = new StringBuilder();
        result.append(getResultHeader(fileReader.getTableName()));
        while(resultSet.next()) {
            for(int i = 1; i <= getColumnCountOfTable(fileReader.getTableName()); i++) {
                rowResult[i-1] = resultSet.getObject(i).toString();
                if (i == getColumnCountOfTable(fileReader.getTableName())) {

                    result.append("\n");
                }
            }
            result.append(String.format(getResultFormat(),rowResult));
        }
        return result.toString();
    }

    public String getAllRowsByTableName(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String result = "";
        String query =  buildSelectQuery(false, tableName, null);

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            result += resultStringBuilder(resultSet);
        }
        return result;
    }



    private String getResultFormat() throws SQLException {
        StringBuilder resultFormat = new StringBuilder();
        ArrayList<String> maxLengthOfColumn = getMaxLengthOfColumnsByTableName();
        for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
            resultFormat.append("%-").append(maxLengthOfColumn.get(i)).append("s | ");
        }

        return resultFormat.toString();
    }

    private ArrayList<String> getMaxLengthOfColumnsByTableName() throws SQLException {
        ArrayList<String> formatLengthForAllColumns = new ArrayList<>();
        ArrayList<String> displayNames = fileReader.getDisplayNames();
        StringBuilder query = new StringBuilder();
        query.append(createMaxLengthSelect());

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            //TODO don't do this at home kids
            while (resultSet.next()) {
                for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
                    String maxLength = resultSet.getObject(i+1).toString();
                    if(Integer.parseInt(maxLength) < displayNames.get(i).length()) {
                        maxLength = "" + displayNames.get(i).length();
                    }
                    formatLengthForAllColumns.add(maxLength);
                }
            }
        }
        return formatLengthForAllColumns;
    }

    private String createMaxLengthSelect() {
        StringBuilder query = new StringBuilder("SELECT ");
        //Build bigger select and run that query, take all results into an array
        for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
            query.append("max(length(").append(fileReader.getColumnNames().get(i)).append("))");
            if(i < fileReader.getTableColumnCount()-1) {
                query.append(", ");
            }
        }
        query.append("\nFROM ").append(fileReader.getTableName()).append(";");
        return query.toString();
    }

    private ResultSetMetaData getResultSetMetaDataForEntireTable(String tableName) throws SQLException {
        String query = "SELECT * FROM " + tableName + ";";
        ResultSetMetaData resultSetMetaData;

        MysqlDataSource dataSource = getDatabaseConnection().getDataSource();
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            resultSetMetaData = rs.getMetaData();
        }
        return resultSetMetaData;
    }

    //Not in use as of right now
    private boolean checkIfTableExists(String tableName) throws SQLException {
        boolean exists = false;
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
            if(rs.next()) {
                exists = true;
            }
        }
        return exists;
    }

    private void dropTable(String tableName) throws SQLException {
        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS "+tableName);
        }
    }

    private void createDatabase() throws SQLException{
        try (Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.execute("CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1;");
        }
    }

    private void createTableFromMetaData(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));

        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + fileReader.getTableName() + "(\n");

        try(Connection connection = getDatabaseConnection().getDataSource().getConnection()) {
            Statement statement = connection.createStatement();
            for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
                createTableQuery.append(fileReader.getColumnNames().get(i));
                createTableQuery.append(" ");
                createTableQuery.append(fileReader.getColumnSQLValues().get(i));

                if(i < fileReader.getTableColumnCount() - 1) {
                    createTableQuery.append(",\n");
                }

                if(i == fileReader.getTableColumnCount() - 1) {
                    //TODO fix Temporary solution for primary keys
                    if(fileReader.getAmountOfPrimaryKeys() > 0) {
                        createTableQuery.append(",\n");
                        createTableQuery.append(addPrimaryKeyToQuery(i));
                    }
                    //TODO Missing foreign key fix.
                    //TODO to implement foreign key, FileReader has to be changed
                    //Add a foreign key if and loop
                }
            }
            createTableQuery.append(");");
            statement.executeUpdate(createTableQuery.toString());
        }
    }

    private String addPrimaryKeyToQuery(int index) {
        StringBuilder addPrimaryKeyToQuery = new StringBuilder();
        addPrimaryKeyToQuery.append("PRIMARY KEY(");
        for(int j = 1; j < fileReader.getAmountOfPrimaryKeys() + 1; j++) {
            addPrimaryKeyToQuery.append(fileReader.getColumnSQLValues().get(index + j));
            if(j < fileReader.getAmountOfPrimaryKeys()) {
                addPrimaryKeyToQuery.append(", ");
            }
        }
        addPrimaryKeyToQuery.append(")");
        if(fileReader.getAmountOfForeignKeys() > 0) {
            addPrimaryKeyToQuery.append(",");
        }
        return addPrimaryKeyToQuery.toString();
    }

    protected DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    protected String getPropertyFilePath() {
        return propertyFilePath;
    }

    protected void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }

    protected void startDatabase() throws IOException {
        databaseConnection.databaseBuilder(getPropertyFilePath());
    }

}


