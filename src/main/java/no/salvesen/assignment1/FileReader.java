package no.salvesen.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private String tableName;
    private int tableColumnCount;
    private int amountOfPrimaryKeys;
    private int amountOfForeignKeys;

    private ArrayList<String> columnNames;
    private ArrayList<String> columnSQLValues;
    private ArrayList<String> displayNames;
    private ArrayList<String> insertionValues;

    public FileReader() {
    }

    public void readFile(String filepath) throws FileNotFoundException {
        columnNames = new ArrayList<>();
        columnSQLValues = new ArrayList<>();
        displayNames = new ArrayList<>();
        insertionValues = new ArrayList<>();

        //TODO change source when testing is complete
        Scanner fileParser = new Scanner(new File(filepath));
        fileParser.useDelimiter(";|\\r\\n|\\n");

        readMetaDataFromFile(fileParser);
        readDisplayNamesFromFile(fileParser);
        readInsertValuesFromFile(fileParser);
    }


    /**
     * Description of file:
     * First line: tableName;columnCount;primaryKeys;foreignKeys
     * Second line: columnName * columnCount
     * Third line: MySQL values * columnCount
     * Fourth line: Display Names * columnCount
     * Fifth -> lines: Insertion values
     */
    private void readMetaDataFromFile(Scanner fileParser) {

        setTableName(fileParser.next());
        setTableColumnCount(fileParser.nextInt());
        setAmountOfPrimaryKeys(fileParser.nextInt());
        setAmountOfForeignKeys(fileParser.nextInt());


        for(int i = 0; i < getTableColumnCount(); i++) {
            columnNames.add(fileParser.next());
        }

        for(int i = 0; i < getTableColumnCount()  + getAmountOfPrimaryKeys() + getAmountOfForeignKeys(); i++) {
            columnSQLValues.add(fileParser.next());
        }
    }


    private void readDisplayNamesFromFile(Scanner fileParser) {
        for(int i = 0; i < getTableColumnCount(); i++) {
            displayNames.add(fileParser.next());
        }
    }

    //Fills an ArrayList with the information - When getting the information in the preperation of the insert
    //Assert that size of this array is divisible by columnCount % = 0;
    private void readInsertValuesFromFile(Scanner fileParser) {
        while(fileParser.hasNext()) {
            for (int i = 0; i < getTableColumnCount(); i++) {
                insertionValues.add(fileParser.next());
            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getTableColumnCount() {
        return tableColumnCount;
    }

    public void setTableColumnCount(int tableColumnCount) {
        this.tableColumnCount = tableColumnCount;
    }

    public int getAmountOfPrimaryKeys() {
        return amountOfPrimaryKeys;
    }

    public void setAmountOfPrimaryKeys(int amountOfPrimaryKeys) {
        this.amountOfPrimaryKeys = amountOfPrimaryKeys;
    }

    public int getAmountOfForeignKeys() {
        return amountOfForeignKeys;
    }

    public void setAmountOfForeignKeys(int amountOfForeignKeys) {
        this.amountOfForeignKeys = amountOfForeignKeys;
    }

    public ArrayList<String> getColumnSQLValues() {
        return columnSQLValues;
    }

    public ArrayList<String> getDisplayNames() {
        return displayNames;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public ArrayList<String> getInsertionValues() {
        return insertionValues;
    }
}
