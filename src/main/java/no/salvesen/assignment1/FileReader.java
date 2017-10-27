package no.salvesen.assignment1;

import java.util.ArrayList;

public class FileReader {

    private String tableName;
    private int columnCount;
    private int amountOfPrimaryKeys;
    private int amountOfForeignKeys;

    private ArrayList<String> columnSQLValues;
    private ArrayList<String> displayNames;

    public void readFile() {

    }


    /**
     * Description of file:
     * First line: tableName;columnCount;primaryKeys;foreignKeys
     * Second line: columnName * columnCount
     * Third line: MySQL values * columnCount
     * Fourth line: Display Names * columnCount
     * Fifth -> lines: Insertion values
     */
    private void readMetaDataFromFile() {

    }


    private void readDisplayNamesFromFile() {

    }

    private void readInsertValuesFromFile() {

    }

/*    private File subjectFile;
    private File lecturerFile;
    private File roomFile;

    public FileReader(File subjectFile, File lecturerFile, File roomFile) {
        setSubjectFile(subjectFile);
        setLecturerFile(lecturerFile);
        setRoomFile(roomFile);
    }




    public File getSubjectFile() {
        return subjectFile;
    }

    public void setSubjectFile(File subjectFile) {
        this.subjectFile = subjectFile;
    }

    public File getLecturerFile() {
        return lecturerFile;
    }

    public void setLecturerFile(File lecturerFile) {
        this.lecturerFile = lecturerFile;
    }

    public File getRoomFile() {
        return roomFile;
    }

    public void setRoomFile(File roomFile) {
        this.roomFile = roomFile;
    }*/
}
