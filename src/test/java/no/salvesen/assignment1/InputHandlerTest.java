package no.salvesen.assignment1;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.io.*;
import java.util.Properties;

import static junit.framework.TestCase.assertTrue;

public class InputHandlerTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    private final String LINE_BREAK = System.getProperty("line.separator");

    private final String USER_INPUT_ON_MAIN_MENU_TO_GO_TO_SEARCH = "1" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_SUBJECT = "1" + LINE_BREAK + "pgr200" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_ALL_SUBJECTS = "2" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_LECTURER = "3" + LINE_BREAK + "Per Lauvas" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_ALL_LECTURERS = "4" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_A_ROOM = "5" + LINE_BREAK + "F101" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_ALL_ROOMS = "6" + LINE_BREAK;
    private final String USER_INPUT_TO_SEARCH_FOR_SUBJECT_WITH_LECTURER = "7" + LINE_BREAK;
    private final String USER_INPUT_TO_RETURN_TO_MAIN_MENU_FROM_SEARCH_MENU = "8" + LINE_BREAK;

    private final String USER_INPUT_ON_MAIN_MENU_TO_UPDATE_TABLE = "2" + LINE_BREAK;
    private final String USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_SUBJECT_FILE = "1" + LINE_BREAK + "src/test/Test_table_files/subject_test_file.csv" + LINE_BREAK;
    private final String USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_ROOM_FILE = "2" + LINE_BREAK + "src/test/Test_table_files/room_test_file.csv" + LINE_BREAK;
    private final String USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_LECTURER_FILE = "3" + LINE_BREAK + "src/test/Test_table_files/lecturer_test_file.csv" + LINE_BREAK;
    private final String USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_LECTURER_IN_SUBJECT_FILE = "4" + LINE_BREAK + "src/test/Test_table_files/lecturer_in_subject_test_file.csv" + LINE_BREAK;
    private final String USER_INPUT_TO_USE_EXISTING_FILES_IN_FILES_FOLDER = "6" + LINE_BREAK;
    private final String USER_INPUT_TO_FILL_TABLE_WITH_INFORMATION_FROM_FILES = "5" + LINE_BREAK + "subject" + LINE_BREAK + "Y" + LINE_BREAK;
    private final String USER_INPUT_TO_RETURN_TO_MAIN_MENU_FROM_UPDATE_TABLE_MENU = "7" + LINE_BREAK;
    private final String USER_INPUT_TO_GO_TO_SEARCH_MENU_AND_EXIT = "1" + LINE_BREAK + "9" + LINE_BREAK;

    public InputHandlerTest() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        Properties properties = new Properties();

        /**
         * Properties file needs to be updated prior to testing.
         */
        InputStream input = new FileInputStream("src/test/files/testDatabaseLogin.properties");

        properties.load(input);

        //Source:https://stackoverflow.com/questions/23653875/how-to-simulate-multiple-user-input-for-junit
        String serverName = properties.getProperty("serverName");
        String databaseName = properties.getProperty("databaseName");
        String databaseUserName = properties.getProperty("databaseUser");
        String databaseUserPassword = properties.getProperty("databasePassword");

        String USER_INPUT_ON_PROPERTIES_MENU = "3" + LINE_BREAK + serverName + LINE_BREAK + databaseName + LINE_BREAK + databaseUserName + LINE_BREAK + databaseUserPassword + LINE_BREAK + "N" + LINE_BREAK + "Y" + LINE_BREAK;

        String allMenuOptionsSimulatedUserInput = USER_INPUT_ON_PROPERTIES_MENU + USER_INPUT_ON_MAIN_MENU_TO_GO_TO_SEARCH +
                USER_INPUT_TO_SEARCH_FOR_SUBJECT + USER_INPUT_TO_SEARCH_FOR_ALL_SUBJECTS +
                USER_INPUT_TO_SEARCH_FOR_LECTURER + USER_INPUT_TO_SEARCH_FOR_ALL_LECTURERS +
                USER_INPUT_TO_SEARCH_FOR_A_ROOM + USER_INPUT_TO_SEARCH_FOR_ALL_ROOMS +
                USER_INPUT_TO_SEARCH_FOR_SUBJECT_WITH_LECTURER + USER_INPUT_TO_RETURN_TO_MAIN_MENU_FROM_SEARCH_MENU +
                USER_INPUT_ON_MAIN_MENU_TO_UPDATE_TABLE + USER_INPUT_TO_USE_EXISTING_FILES_IN_FILES_FOLDER +
                USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_SUBJECT_FILE + USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_ROOM_FILE +
                USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_LECTURER_FILE + USER_INPUT_TO_ADD_NEW_FILEPATH_FOR_LECTURER_IN_SUBJECT_FILE +
                USER_INPUT_TO_FILL_TABLE_WITH_INFORMATION_FROM_FILES + USER_INPUT_TO_RETURN_TO_MAIN_MENU_FROM_UPDATE_TABLE_MENU +
                USER_INPUT_TO_GO_TO_SEARCH_MENU_AND_EXIT;

        ByteArrayInputStream in = new ByteArrayInputStream(allMenuOptionsSimulatedUserInput.getBytes());

        System.setIn(in);
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void startMenuLoop() throws Exception {
        InputHandler inputHandler = new InputHandler();
        Menu menu = new Menu();
        inputHandler.startMenuLoop();

        String printedByApplication = outputStream.toString();

        for (String menuOption : menu.getMainMenuChoices()) {
            assertTrue(printedByApplication.contains(menuOption));
        }
        for (String menuOption : menu.getSearchChoices()) {
            assertTrue(printedByApplication.contains(menuOption));
        }
        for (String menuOption : menu.getTableChoices()) {
            assertTrue(printedByApplication.contains(menuOption));
        }
        for (String menuOption : menu.getPropertiesMenuChoices()) {
            assertTrue(printedByApplication.contains(menuOption));
        }
    }

    @After
    public void tearDown() {
        System.setIn(System.in);
    }
}