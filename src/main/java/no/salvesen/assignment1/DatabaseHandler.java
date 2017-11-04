package no.salvesen.assignment1;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseHandler{

    private DatabaseConnection databaseConnection;
    private FileReader fileReader;
    private String propertyFilePath;
    private ArrayList<String> foreignKeysToBeAdded;

    public  DatabaseHandler()  {
        databaseConnection = new DatabaseConnection();
        fileReader  = new FileReader();
        foreignKeysToBeAdded = new ArrayList<>();

    }


    //TODO Create database if not already exists
    //TODO

    //TODO Move this
    private String getResultHeader(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String[] columnDisplayNames = new String[fileReader.getTableColumnCount()];
        for (int i = 0; i < columnDisplayNames.length; i++) {
            columnDisplayNames[i] = fileReader.getDisplayNames().get(i);
        }
        return String.format(getResultFormat(), columnDisplayNames);

    }


    public void tearDownDatabaseAndSetBackUp() throws SQLException, IOException {

        String subjectTable = "subject";
        String roomTable = "room";
        String lecturerTable = "lecturer";

        dropTable(subjectTable);
        dropTable(roomTable);
        dropTable(lecturerTable);

        createDatabase();

        createTableFromMetaData(subjectTable);
        createTableFromMetaData(roomTable);
        createTableFromMetaData(lecturerTable);

        //TODO Works in theory. Test databases has to be set up to check
        addAllForeignKeysToTables();

        fillTableFromFileByTableName(subjectTable);
        fillTableFromFileByTableName(roomTable);
        fillTableFromFileByTableName(lecturerTable);

    }

    public void tearDownTableAndSetBackUpWithNewInformation(String tableName) throws SQLException, FileNotFoundException {
        dropTable(tableName);
        createTableFromMetaData(tableName);
        fillTableFromFileByTableName(tableName);
    }

    public ArrayList<String> getArrayListOfTableNames() throws SQLException {

        ArrayList<String> tableNames = new ArrayList<>();

        try(Connection connection = databaseConnection.getConnection()) {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet rs = databaseMetaData.getTables(null, null, "%", null);
            while (rs.next()) {
                tableNames.add(rs.getString(3));
            }
        }
        return tableNames;
    }


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

    /**
     * Puts together a prepared statement based on a table name.
     *
     * @param tableName
     * @return a prepared query
     * @throws SQLException
     */

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

        try(Connection connection = this.databaseConnection.getConnection()) {
            String preparedInsert = prepareInsertStatementBasedOnMetaData(tableName);
            PreparedStatement preparedStatement = connection.prepareStatement(preparedInsert);

            int index = 1;
            int counter = 0;

            while(counter < insertionValues.size()) {
                while(index < fileReader.getTableColumnCount() + 1) {
                    preparedStatement.setObject(index,insertionValues.get(counter));
                    index++;
                    counter++;
                }
                preparedStatement.addBatch();
                index = 1;
            }
            preparedStatement.executeBatch();
        }
    }


    private void addAllForeignKeysToTables() throws SQLException {
        try(Connection connection = this.databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            for(String foreignKeyQuery : foreignKeysToBeAdded){
                statement.addBatch(foreignKeyQuery);
            }
            statement.executeBatch();

        }
    }

    public String getRowsFromTableByColumnNameAndSearchColumnValue(String tableName, String columnName, String columnValue) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));
        String result = "";
        String query =  buildSelectQuery(true, tableName, columnName);

        try(Connection connection = this.databaseConnection.getConnection();
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

        try(Connection connection = this.databaseConnection.getConnection();
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

        try(Connection connection = this.databaseConnection.getConnection();
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

        try(Connection connection = this.databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            resultSetMetaData = rs.getMetaData();
        }
        return resultSetMetaData;
    }

    private void dropTable(String tableName) throws SQLException {
        try(Connection connection = databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS "+tableName);
        }
    }

    public void createDatabase() throws SQLException, IOException {
        try(Connection connection = databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1;";
            stmt.executeUpdate(query);
        }
    }

    public void createTestDatabase() throws SQLException, IOException {
        try(Connection connection = databaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            String query = "CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1_testing;";
            stmt.executeUpdate(query);
        }
    }
//TODO made public to test
    public void createTableFromMetaData(String tableName) throws FileNotFoundException, SQLException {
        fileReader.readFile(fileReader.getFileByTableName(tableName));

        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + fileReader.getTableName() + "(\n");

        try(Connection connection = databaseConnection.getConnection()) {
            Statement statement = connection.createStatement();
            for(int i = 0; i < fileReader.getTableColumnCount(); i++) {
                createTableQuery.append(fileReader.getColumnNames().get(i));
                createTableQuery.append(" ");
                createTableQuery.append(fileReader.getColumnSQLValues().get(i));

                if(i < fileReader.getTableColumnCount() - 1) {
                    createTableQuery.append(",\n");
                }

                if(i == fileReader.getTableColumnCount() - 1) {
                    ///Syntax for multiple primary keys -> PRIMARY KEY(pk1, pk2, pk3
                    if(fileReader.getAmountOfPrimaryKeys() > 0) {
                        createTableQuery.append(",\n");
                        createTableQuery.append(addPrimaryKeyToQuery(i));
                    }
                    if(fileReader.getAmountOfForeignKeys() > 0) {
                        addForeignKeyToList(i);
                    }
                }
            }
            createTableQuery.append(");");
            statement.executeUpdate(createTableQuery.toString());
        }
    }

    private String addPrimaryKeyToQuery(int index) {
        StringBuilder primaryKeysToBeAddedToQuery = new StringBuilder();
        primaryKeysToBeAddedToQuery.append("PRIMARY KEY(");
        for(int i = 1; i < fileReader.getAmountOfPrimaryKeys() + 1; i++) {
            primaryKeysToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(index + i));
            if(i < fileReader.getAmountOfPrimaryKeys()) {
                primaryKeysToBeAddedToQuery.append(", ");
            }
        }
        primaryKeysToBeAddedToQuery.append(")");
        return primaryKeysToBeAddedToQuery.toString();
    }

    private void addForeignKeyToList(int index) {
        StringBuilder foreignKeyToBeAddedToQuery = new StringBuilder();
        for(int i = 1; i < fileReader.getAmountOfForeignKeys() + 1; i++) {
            foreignKeyToBeAddedToQuery.append("ALTER TABLE ").append(fileReader.getTableName()).append("\n");
            foreignKeyToBeAddedToQuery.append("ADD FOREIGN KEY (");
            foreignKeyToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(index + fileReader.getAmountOfPrimaryKeys() + i));
            foreignKeyToBeAddedToQuery.append(") REFERENCES ");
            foreignKeyToBeAddedToQuery.append(fileReader.getColumnSQLValues().get(index + fileReader.getAmountOfPrimaryKeys() + i + 1));
            foreignKeyToBeAddedToQuery.append(";");
            foreignKeysToBeAdded.add(foreignKeyToBeAddedToQuery.toString());
        }
    }

    private void getDatabaseNameFromProperties() {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(propertyFilePath);

        properties.load(input);

        dataSource.setServerName(properties.getProperty("serverName"));
        dataSource.setDatabaseName(properties.getProperty("databaseName"));
    }

    public void startConnection() throws IOException {
        databaseConnection.databaseBuilder(getPropertyFilePath());
    }

    private String getPropertyFilePath() {
        return propertyFilePath;
    }

    protected void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }




}