package no.salvesen.assignment1;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class InputHandler {


    private Menu menu;
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;

    private Scanner userInput;

    public InputHandler() throws IOException {
        fileReader = new FileReader();

        userInput = new Scanner(System.in);
        databaseHandler = new DatabaseHandler();
        menu = new Menu();
    }


    private void setUpProperties() throws IOException, SQLException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            System.out.println(menu.propertiesMenu());
            menuChoice = userInput.nextLine();
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
                    System.out.println("Server name: ");
                    String serverName = userInput.nextLine();
                    System.out.println("Database name: ");
                    String databaseName = userInput.nextLine();
                    System.out.println("Username: ");
                    String databaseUser = userInput.nextLine();
                    System.out.println("Password: ");
                    String databasePassword = userInput.nextLine();

                    //Filling property file
                    properties.setProperty("serverName", serverName);
                    properties.setProperty("databaseName", databaseName);
                    properties.setProperty("databaseUser", databaseUser);
                    properties.setProperty("databasePassword", databasePassword);
                    File userEnteredProperties = new File("./src/files/userEnteredDatabaseLogin.properties");

                    try(FileOutputStream fileOut = new FileOutputStream(userEnteredProperties)) {
                        properties.store(fileOut, "Added by user");
                        System.out.println("Property file set up. Attempting to connect.\n");
                        databaseHandler.setPropertyFilePath("./src/files/userEnteredDatabaseLogin.properties");
                        finished = true;
                    }
                    break;

                default:
                    System.out.println("Incorrect choice, please try again.");
                    break;

            }
        }

        //Starts database with the properties chosen.
        databaseHandler.startDatabase();

    }

    public void startMenuLoop() throws IOException, SQLException {
        setUpProperties();
        databaseHandler.tearDownDatabaseAndSetBackUp();

        showMainMenu();
    }

    private void showMainMenu() throws IOException, SQLException {
        String menuChoice;
        while(true) {
            System.out.println(menu.mainMenu());
            menuChoice = userInput.nextLine();

            switch(menuChoice) {
                case "1":
                    showSearchMenu();
                    break;
                case "2":
                    showTableMenu();
                    break;
                default:
                    System.out.println("Incorrect choice, please try again.");
            }
        }
    }

    //TODO missing option regarding filling information into DB
    private void showTableMenu() throws IOException, SQLException {
        String menuChoice;
        while(true) {
            System.out.println(menu.tableMenu());
            menuChoice = userInput.nextLine();
            String filePathMessage = "Please enter the file-path to the csv file.";

            switch(menuChoice) {
                case "1":
                    System.out.println(filePathMessage);
                    fileReader.setSubjectFile(new File(userInput.nextLine()));
                    break;
                case "2":
                    System.out.println(filePathMessage);
                    fileReader.setRoomFile(new File(userInput.nextLine()));
                    break;
                case "3":
                    System.out.println(filePathMessage);
                    fileReader.setLecturerFile(new File(userInput.nextLine()));
                    break;
                case "4":
                    System.out.println("Existing files chosen");
                    break;
                case "5":
 /*                   System.out.println("Which table do you want to tear down and fill with information?");
                    System.out.println("Possible tables are: " );
                    printAllTableNames();*/
//                    String tableName = userInput.nextLine();
//                    databaseHandler.fillTableFromFileByTableName(tableName);
                    chooseTableToFillWithInformation();
                    System.out.println("Cleared table and filled with information from file.");
                    break;
                case "6":
                    showMainMenu();
                    break;
                case "7":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again.");
            }

        }
    }

    private void showSearchMenu() throws IOException, SQLException {
        String menuChoice;
        while(true) {
            System.out.println(menu.searchMenu());
            menuChoice = userInput.nextLine();

            switch(menuChoice) {
                case "1":
                    System.out.println("Please enter subject code: ");
                    String subject = userInput.nextLine();
                    System.out.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("subject","code",subject));
                    break;
                case "2":
                    System.out.println(databaseHandler.getAllRowsByTableName("subject"));
                    break;
                case "3":
                    System.out.println("Please enter name of lecturer: ");
                    String lecturer = userInput.nextLine();
                    System.out.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("lecturer","name",lecturer));

                    break;
                case "4":
                    System.out.println(databaseHandler.getAllRowsByTableName("lecturer"));

                    break;
                case "5":
                    System.out.println("Please enter name of room: ");
                    String room = userInput.nextLine();
                    System.out.println(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("room","name",room));

                    break;
                case "6":
                    System.out.println(databaseHandler.getAllRowsByTableName("room"));

                    break;
                case "7":
                    showMainMenu();
                    break;
                case "8":
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Incorrect choice, please try again.");
            }

        }
    }

    private void printAllTableNames() throws SQLException {
        for(String tableName : databaseHandler.getArrayListOfTableNames()) {
            System.out.println(tableName);
        }
    }

    private void chooseTableToFillWithInformation() throws SQLException, FileNotFoundException {
        String chosenTable;
        ArrayList<String> tableNames = databaseHandler.getArrayListOfTableNames();
        while (true) {
            System.out.println("Possible tables are: ");
            printAllTableNames();
            chosenTable = userInput.nextLine();
            for (String tableName : tableNames) {
                if (chosenTable.equals(tableName)) {
                    databaseHandler.tearDownTableAndSetBackUpWithNewInformation(chosenTable);
                    return;
                }
            }
        }
    }
}