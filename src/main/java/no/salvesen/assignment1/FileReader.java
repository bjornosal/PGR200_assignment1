package no.salvesen.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {

    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    private String tableName;
    private int tableColumnCount;
    private int amountOfPrimaryKeys;
    private int amountOfForeignKeys;

    private ArrayList<String> columnNames;
    private ArrayList<String> columnSQLValues;
    private ArrayList<String> displayNames;
    private ArrayList<String> insertionValues;

    public FileReader() {
        setSubjectFile(new File("src/files/subject.csv"));
        setRoomFile(new File("src/files/room.csv"));
        setLecturerFile(new File("src/files/lecturer.csv"));
    }

    public void readFile(File tableFile) throws FileNotFoundException {
        columnNames = new ArrayList<>();
        columnSQLValues = new ArrayList<>();
        displayNames = new ArrayList<>();
        insertionValues = new ArrayList<>();

        Scanner fileParser = new Scanner(tableFile);
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

    public File getFileByTableName(String tableName) {
        tableName = tableName.toLowerCase();
        switch(tableName) {
            case "subject":
                return getSubjectFile();
            case "room":
                return getRoomFile();
            case "lecturer":
                return getLecturerFile();
        }


        return null;
    }


    public File getSubjectFile() {
        return subjectFile;
    }

    public void setSubjectFile(File subjectFile) {
        this.subjectFile = subjectFile;
    }

    public File getRoomFile() {
        return roomFile;
    }

    public void setRoomFile(File roomFile) {
        this.roomFile = roomFile;
    }

    public File getLecturerFile() {
        return lecturerFile;
    }

    public void setLecturerFile(File lecturerFile) {
        this.lecturerFile = lecturerFile;
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
