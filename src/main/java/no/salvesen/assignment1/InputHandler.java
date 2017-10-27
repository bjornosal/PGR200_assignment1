package no.salvesen.assignment1;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class InputHandler {


    private Menu menu;
    private DatabaseHandler databaseHandler;

    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    public InputHandler() throws IOException {
        setSubjectFile(new File("src/files/subject.csv"));
        setRoomFile(new File("src/files/room.csv"));
        setLecturerFile(new File("src/files/lecturer.csv"));

        databaseHandler = new DatabaseHandler();
        menu = new Menu();
    }


    private void setUpProperties(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            outputToClient.println(menu.propertiesMenu());
            menuChoice = inputFromClient.readLine();
            switch(menuChoice) {

                //Use default properties
                case "1":
                    databaseHandler.setPropertyFilePath("./src/files/defaultDatabaseLogin.properties");
                    finished = true;
                    break;

                //use properties previously set by user
                case "2":
                    databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");
                    finished = true;
                    break;

                //Enter new properties
                case "3":
                    outputToClient.println("Server name: ");
                    String serverName = inputFromClient.readLine();
                    outputToClient.println("Database name: ");
                    String databaseName = inputFromClient.readLine();
                    outputToClient.println("Username: ");
                    String databaseUser = inputFromClient.readLine();
                    outputToClient.println("Password: ");
                    String databasePassword = inputFromClient.readLine();

                    //Filling property file
                    properties.setProperty("serverName", serverName);
                    properties.setProperty("databaseName", databaseName);
                    properties.setProperty("databaseUser", databaseUser);
                    properties.setProperty("databasePassword", databasePassword);
                    File userEnteredProperties = new File("./src/files/userEnteredDatabaseLogin.properties");

                    try(FileOutputStream fileOut = new FileOutputStream(userEnteredProperties)) {
                        properties.store(fileOut, "Added by user");
                        outputToClient.println("Property file set up. Attempting to connect.\n");
                        databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");
                        finished = true;
                    }
                    break;

                default:
                    outputToClient.println("Incorrect choice, please try again.");
                    break;

            }
        }

        //Starts database with the properties chosen.
        databaseHandler.startDatabase();

    }

    public void startMenuLoop(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        setUpProperties(outputToClient, inputFromClient);

        //Temporary setup for testing
        databaseHandler.tearDownDatabaseAndSetBackUp(getSubjectFile(),getRoomFile(),getLecturerFile());

        showMainMenu(outputToClient, inputFromClient);
    }

    private void showMainMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.mainMenu());
            menuChoice = inputFromClient.readLine();

            switch(menuChoice) {
                case "1":
                    showSearchMenu(outputToClient, inputFromClient);
                    break;
                case "2":
                    showTableMenu(outputToClient, inputFromClient);
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }
        }
    }

    //TODO missing option regarding filling information into DB
    private void showTableMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.tableMenu());
            menuChoice = inputFromClient.readLine();
            String filePathMessage = "Please enter the file-path to the csv file.";

            switch(menuChoice) {
                case "1":
                    outputToClient.println(filePathMessage);
                    setSubjectFile(new File(inputFromClient.readLine()));
                    break;
                case "2":
                    outputToClient.println(filePathMessage);
                    setRoomFile(new File(inputFromClient.readLine()));
                    break;
                case "3":
                    outputToClient.println(filePathMessage);
                    setLecturerFile(new File(inputFromClient.readLine()));
                    break;
                case "4":
                    outputToClient.println("Existing files chosen");
                    break;
                case "5":
                    //TODO add fillTable method here
                    outputToClient.print("Cleared tables and filled with information from files.");
                    break;
                case "6":
                    showMainMenu(outputToClient, inputFromClient);
                    break;
                case "7":
                    //Closes thread
//                    Thread.currentThread().interrupt();
//                    return;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
    }

    private void showSearchMenu(PrintWriter outputToClient, BufferedReader inputFromClient) throws IOException, SQLException {
        String menuChoice;
        while(true) {
            outputToClient.println(menu.searchMenu());
            menuChoice = inputFromClient.readLine();

            switch(menuChoice) {
                case "1":
                    outputToClient.println("Please enter subject code: ");
                    String subject = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getSubjectRowBySubjectID(subject));
                    break;
                case "2":
                    outputToClient.println(databaseHandler.getAllRowsFromSubjectTable());
                    break;
                case "3":
                    outputToClient.println("Please enter name of lecturer: ");
                    String lecturer = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getLecturerRowByName(lecturer));
                    break;
                case "4":
                    outputToClient.println(databaseHandler.getAllRowsFromLecturerTable());
                    break;
                case "5":
                    outputToClient.println("Please enter name of room: ");
                    String room = inputFromClient.readLine();
                    outputToClient.println(databaseHandler.getRoomRowByName(room));
                    break;
                case "6":
                    outputToClient.println(databaseHandler.getAllRowsFromRoomTable());
                    break;
                case "7":
                    showMainMenu(outputToClient,inputFromClient);
                    break;
                case "8":
                    //Closes thread
//                    Thread.currentThread().interrupt();
//                    return;
                    break;
                default:
                    outputToClient.println("Incorrect choice, please try again.");
            }

        }
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

/*
    private DatabaseHandler databaseHandler;

    private File currentFile;
    private File subjectFile;
    private File roomFile;
    private File lecturerFile;

    private int tablePick;
    private String tableName;
    private String filePath;

    public InputHandler() throws SQLException, IOException {
        databaseHandler = new DatabaseHandler();
        setSubjectFile(new File("src/files/subject.csv"));
        setRoomFile(new File("src/files/room.csv"));
        setLecturerFile(new File("src/files/lecturer.csv"));
    }

    public void fileChooser() {
        Scanner userInput = new Scanner(System.in);
        System.out.printf("%10s");
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

    */
}