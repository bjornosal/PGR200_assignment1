package no.salvesen.assignment1;

import com.mysql.jdbc.CommunicationsException;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class InputHandler {


    private Menu menu;
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;
    private ExceptionHandler exceptionHandler;
    private final String defaultPropertiesFilePath = "src/files/defaultDatabaseConfig.properties";
    private final String userEnteredPropertiesFilePath = "src/files/userEnteredDatabaseLogin.properties";
    private final String webServerPropertiesFilePath = "src/files/webServerLogin.properties";

    private Scanner userInput;

    public InputHandler()  {
        fileReader = new FileReader();
        exceptionHandler = new ExceptionHandler();

        userInput = new Scanner(System.in);
        databaseHandler = new DatabaseHandler();
        menu = new Menu();
    }


    private void setUpProperties() throws IOException {
        boolean finished = false;
        String menuChoice;

        while (!finished) {
            Properties properties = new Properties();
            System.out.println(menu.propertiesMenu());
            menuChoice = userInput.nextLine();
            switch(menuChoice) {

                //Use default properties
                case "1":
                    databaseHandler.setPropertyFilePath(defaultPropertiesFilePath);
                    finished = true;
                    break;
                case "2":
                    databaseHandler.setPropertyFilePath(webServerPropertiesFilePath);
                    finished = true;
                    break;
                //use properties previously set by user
                case "3":
                    databaseHandler.setPropertyFilePath(userEnteredPropertiesFilePath);
                    finished = true;
                    break;

                //Enter new properties
                case "4":
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
                    File userEnteredProperties = new File(userEnteredPropertiesFilePath);

                    try(FileOutputStream fileOut = new FileOutputStream(userEnteredProperties)) {
                        properties.store(fileOut, "Added by user");
                        System.out.println("Property file set up. Attempting to connect.\n");
                        databaseHandler.setPropertyFilePath(userEnteredPropertiesFilePath);
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

    public void startMenuLoop() {
        boolean connected = false;

        while (!connected) {
            try {
                setUpProperties();
            } catch (IOException e) {
                exceptionHandler.outputIOException("WriteProp");
            }

            try {
                databaseHandler.tearDownDatabaseAndSetBackUp();
                connected = true;
            } catch (SQLException e) {
                exceptionHandler.outputSQLException("connect");
            } catch (FileNotFoundException e) {
                exceptionHandler.outputFileNotFoundException();
            }
        }
        try {
            showMainMenu();
        } catch (SQLException e) {
            exceptionHandler.outputSQLException("createTable");
        } catch (FileNotFoundException e) {
            exceptionHandler.outputFileNotFoundException();
        }
    }


    private void showMainMenu() throws FileNotFoundException, SQLException {
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
    private void showTableMenu() throws FileNotFoundException, SQLException {
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

    private void showSearchMenu() throws SQLException, FileNotFoundException {
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