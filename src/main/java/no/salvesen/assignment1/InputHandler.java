package no.salvesen.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Scanner;

public class InputHandler {


    private DatabaseConn dbc;
    private DatabaseHandler databaseHandler;

    private File currentFile;
    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    private int tablePick;
    private String tableName;
    private String filePath;

    public InputHandler() throws SQLException {
        dbc = new DatabaseConn();
        databaseHandler = new DatabaseHandler();
        setSubjectFile(new File("src/files/subject.csv"));
        setRoomFile(new File("src/files/room.csv"));
        setLecturerFile(new File("src/files/lecturer.csv"));
    }

    public void fileChooser() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please choose which table you are entering information for: ");
        System.out.println("1: Subject");
        System.out.println("2: Room");
        System.out.println("3: Lecturer");
        System.out.println("4: Use existing files in files folder");
        setTablePick(userInput.nextInt());


        switch (tablePick) {
            case 1:
                setTableName("subject");
                break;
            case 2:
                setTableName("room");
                break;
            case 3:
                setTableName("lecturer");
                break;
            case 4:
                System.out.println("Existing files chosen.");
                break;
            default:
                System.out.println("Existing files chosen.");
                break;

        }
        if(tablePick == 1 || tablePick == 2 || tablePick == 3) {
            System.out.println("Please copy the file-path to the csv file for " + tableName + "s.");
            //To get input correctly.
            userInput.nextLine();
            setFilePath(userInput.nextLine());
        }

        switch (tablePick) {
            case 1:
                setSubjectFile(new File(filePath));
                setCurrentFile(getSubjectFile());
                break;
            case 2:
                setRoomFile(new File(filePath));
                setCurrentFile(getRoomFile());
                break;
            case 3:
                setLecturerFile(new File(filePath));
                setCurrentFile(getLecturerFile());
                break;
            case 4:
                setSubjectFile(new File("src/files/subject.csv"));
                setRoomFile(new File("src/files/room.csv"));
                setLecturerFile(new File("src/files/lecturer.csv"));
                setCurrentFile(getSubjectFile());
                break;
            default:
                setSubjectFile(new File("src/files/subject.csv"));
                setRoomFile(new File("src/files/room.csv"));
                setLecturerFile(new File("src/files/lecturer.csv"));
                setCurrentFile(getSubjectFile());
                break;
        }
    }

    //To be implemented
    public void existingTable() throws SQLException, FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Table " + getTableName() + " already exists. ");
        System.out.println("Table contains " + databaseHandler.getRowCount(getTableName()) + " rows");
        System.out.println("Drop table and use file information to recreate? (Y/N)");
        String answer = sc.next();

        switch (answer) {
            case "Y":
                System.out.println("Dropping table " + getTableName());
                databaseHandler.dropTable(getTableName());
                databaseHandler.createTable(getTableName());
                databaseHandler.fillTable(getCurrentFile(), getTableName());
                break;
            case "y":
                System.out.println("Dropping table " + getTableName());
                databaseHandler.dropTable(getTableName());
                databaseHandler.createTable(getTableName());
                databaseHandler.fillTable(getCurrentFile(), getTableName());
                break;
            case "N":
                System.out.println("You chose to not drop table.");
                break;
            case "n":
                System.out.println("You chose to not drop table.");
                break;
            default:
                System.out.println("You chose to not drop table.");
                break;
        }
    }

    public void choices() throws SQLException {
        Scanner userInput = new Scanner(System.in);
        boolean finished = false;
        while(!finished) {
            System.out.println("What would you like to do? Enter the number before action and press enter");
            System.out.println("1: Read in information for tables");
            System.out.println("2: Search for subject");
            System.out.println("3: Get information on all subjects");
            System.out.println("4: Quit");

            int choice = userInput.nextInt();

            switch (choice) {
                case 1:
                    fileChooser();
                    break;
                case 2:
                    System.out.print("Enter subject code: ");
                    String subjectCode = userInput.next();
                    searchSubject(subjectCode);
                    break;
                case 3:
                    getAllSubjects();
                    break;
                case 4:
                    finished = true;
                    break;
            }
        }
    }

    private void searchSubject(String subject) throws SQLException {
        System.out.println("Searching for subject " + subject);
        String result = databaseHandler.getSubject(subject);
        if(!result.equals("")) {
            System.out.println(result);
        }
    }

    private void getAllSubjects() throws SQLException {
        System.out.println("Getting information on all subjects.");
        String result = databaseHandler.getAllSubjects();
        if(!result.equals("")) {
            System.out.println(result);
        }
    }


    public int getTablePick() {
        return tablePick;
    }

    public void setTablePick(int tablePick) {
        this.tablePick = tablePick;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }
}