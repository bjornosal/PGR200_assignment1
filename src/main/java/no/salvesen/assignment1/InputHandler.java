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
    private ExceptionHandler exceptionHandler;
    private PropertiesHandler propertiesHandler;

    private final String DEFAULT_PROPERTIES_FILEPATH = "src/files/defaultDatabaseConfig.properties";
    private final String USER_ENTERED_PROPERTIES_FILEPATH = "src/files/userEnteredDatabaseLogin.properties";
    private boolean finished = false;

    private Scanner userInput;

    public InputHandler()  {
        fileReader = new FileReader();
        exceptionHandler = new ExceptionHandler();
        menu = new Menu();
        propertiesHandler = new PropertiesHandler();
        try {
            databaseHandler = new DatabaseHandler(propertiesHandler);
        } catch (IOException e) {
            //TODO fix
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userInput = new Scanner(System.in);
    }


    /**
     * Loop for choices regarding which properties to use to connect to a database.
     * @throws IOException If unable to find the file.
     */
    private void setUpProperties() throws IOException, SQLException {
        String menuChoice;
        boolean setUp = false;
        while (!setUp) {
            Properties properties = new Properties();
            System.out.println(menu.propertiesMenu());
            menuChoice = userInput.nextLine();
            switch(menuChoice) {

                //Use default properties
                case "1":
                    if(!isDefaultDatabaseLoginPropertiesFileIsEmpty()) {
                        setUserProperties(properties);
                    } else {
                        propertiesHandler.setPropertyFilePath(DEFAULT_PROPERTIES_FILEPATH);
                        setUp = true;
                    }
                    break;
                //use properties previously set by user
                case "2":
                    if(!isDefaultDatabaseLoginPropertiesFileIsEmpty()) {
                        setUserProperties(properties);
                    } else {
                        propertiesHandler.setPropertyFilePath(DEFAULT_PROPERTIES_FILEPATH);
                        setUp = true;
                    }
                    break;
                //Enter new properties
                case "3":
                    setUserProperties(properties);
                    setUp = true;
                    break;
                default:
                    System.out.println("Incorrect choice, please try again.");
                    break;

            }
        }
        databaseHandler.startConnection();

        checkAndSetNewDatabaseName();

        try {
            databaseHandler.createDatabase();
        } catch (SQLException e) {
            exceptionHandler.outputDatabaseSQLException();
            setUpProperties();
        }
    }



    /**
     * Starting the loop to put the customer in, which in turn starts the corresponding loops.
     * This causes the customer to be able to go back and forth to whichever menu he wants.
     *
     * Has the responsibility of catching all the exceptions and printing them to the user.
     */
    public void startMenuLoop() {
        boolean connected = false;

        while (!connected && !finished) {
            try {
                setUpProperties();
            } catch (IOException e) {
                exceptionHandler.outputIOException("writeprop");
            } catch (SQLException e) {
                exceptionHandler.outputIOException("createdatabase");
            }
            try {
                databaseHandler.tearDownDatabaseAndSetBackUp();
                connected = true;
            } catch (SQLException e) {
                exceptionHandler.outputSQLException("connect");
            } catch (FileNotFoundException e) {
                exceptionHandler.outputFileNotFoundException();
            } catch (IOException e) {
                //TODO fix
                e.printStackTrace();
            }

            try {
                showMainMenu();
            } catch (SQLException e) {
                exceptionHandler.outputSQLException("createTable");
                //TODO fix
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                exceptionHandler.outputFileNotFoundException();
            }
        }
    }

    /**
     * Starting loop for the main menu.
     * @throws FileNotFoundException If unable to find file.
     * @throws SQLException If unable to get connection.
     */
    private void showMainMenu() throws FileNotFoundException, SQLException {
        String menuChoice;
        while(!finished) {
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

    /**
     * Starting loop for the table menu.
     * @throws FileNotFoundException If unable to find the file.
     * @throws SQLException If unable to get connection.
     */
    private void showTableMenu() throws FileNotFoundException, SQLException {
        String menuChoice;
        while(!finished) {
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
                    System.out.println(filePathMessage);
                    fileReader.setLecturer_in_subject_file(new File(userInput.nextLine()));
                    break;
                case "5":
                    System.out.println("Existing files chosen");
                    fileReader.setSubjectFile(new File("src/files/database files/subject.csv"));
                    fileReader.setRoomFile(new File("src/files/database files/room.csv"));
                    fileReader.setLecturerFile(new File("src/files/database files/lecturer.csv"));
                    fileReader.setLecturer_in_subject_file(new File("src/files/database files/lecturer_in_subject.csv"));
                    break;
                case "6":
                    chooseTableToFillWithInformation();
                    System.out.println("Cleared table and filled with information from file.");
                    break;
                case "7":
                    showMainMenu();
                    break;
                case "8":
                    finished = true;
                    //TODO fix this
//                    System.exit(0);
                    break;
                default:
                    System.out.println("Incorrect choice, please try again.");
            }
        }
    }

    /**
     * Starts loop of the search menu.
     * @throws SQLException If unable to get connection.
     * @throws FileNotFoundException If unable to find the file.
     */
    private void showSearchMenu() throws SQLException, FileNotFoundException {
        String menuChoice;
        while(!finished) {
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
                    System.out.println("Getting information on subjects with lecturer.");
                    System.out.println(databaseHandler.getSubjectNameAndLecturerNameBasedOnPrimaryKeys());
                    break;
                case "8":
                    showMainMenu();
                    break;
                case "9":
                    finished = true;
                    //                    System.exit(0);
                    return;
                default:
                    System.out.println("Incorrect choice, please try again.");
            }

        }
    }

    /**
     * Prints all table names.
     * @throws SQLException If unable to get connection.
     */
    private void printAllTableNames() throws SQLException {
        for(String tableName : databaseHandler.getArrayListOfTableNames()) {
            System.out.println(tableName);
        }
    }

    /**
     * Prints out a list of possible tables to choose from.
     * Until a correct table is chosen, will stay in loop.
     * @throws SQLException If unable to get connection.
     * @throws FileNotFoundException If unable to find the file.
     */
    private void chooseTableToFillWithInformation() throws SQLException, FileNotFoundException {
        String chosenTable;
        ArrayList<String> tableNames = databaseHandler.getArrayListOfTableNames();
        while (true) {
            System.out.println("Possible tables are: ");
            printAllTableNames();
            chosenTable = userInput.nextLine();
            for (String tableName : tableNames) {
                if (chosenTable.equals(tableName)) {
                    if (chosenTable.equalsIgnoreCase("subject") || chosenTable.equalsIgnoreCase("lecturer")) {
                        System.out.println("Lecturer_in_subject table requires this table.");
                        System.out.println("Tear down and set it back up together with " + chosenTable + "-table ?");
                        System.out.println("Y/N");
                        if(userInput.nextLine().equalsIgnoreCase("Y")) {
                            try {
                                databaseHandler.tearDownTableAndSetBackUpWithNewInformation("lecturer_in_subject");
                            } catch (IOException e) {
                                exceptionHandler.outputIOException("fileissue");
                            }
                        } else {
                            break;
                        }
                    }
                    try {
                        databaseHandler.tearDownTableAndSetBackUpWithNewInformation(chosenTable);
                    } catch (IOException e) {
                        exceptionHandler.outputIOException("fileissue");
                    }
                    return;
                }
            }
        }
    }

    private boolean isDefaultDatabaseLoginPropertiesFileIsEmpty() {
        File defaultDatabaseLogin = new File(DEFAULT_PROPERTIES_FILEPATH);
        return defaultDatabaseLogin.length() > 0;
    }

    private boolean isUserEnteredDatabaseLoginPropertiesFileEmpty(){
        File userEnteredDatabaseLogin = new File(USER_ENTERED_PROPERTIES_FILEPATH);
        return userEnteredDatabaseLogin.length() > 0;
    }

    private void checkAndSetNewDatabaseName() throws IOException, SQLException {
        while(databaseHandler.databaseExists()) {
            System.out.println("Database with that name already exists.");
            System.out.println("Create new database? - if not, will overwrite. Y/N");
            if(userInput.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Database name:");
                String databaseName = userInput.nextLine();
                propertiesHandler.setDatabaseNameInProperties(databaseName);
            } else {
                break;
            }
        }
    }

    private void setUserProperties(Properties properties) {
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

        File typeOfLogin;

        System.out.println("Use these properties as default? Y/N");
        if(userInput.nextLine().equalsIgnoreCase("Y")) {
            typeOfLogin = new File(DEFAULT_PROPERTIES_FILEPATH);
        } else {
            typeOfLogin = new File(USER_ENTERED_PROPERTIES_FILEPATH);
        }

        try(FileOutputStream fileOut = new FileOutputStream(typeOfLogin)) {
            properties.store(fileOut, "Added by user");
            System.out.println("Property file set up. Attempting to connect.\n");
            propertiesHandler.setPropertyFilePath(USER_ENTERED_PROPERTIES_FILEPATH);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}