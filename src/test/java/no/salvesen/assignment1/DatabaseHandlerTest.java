package no.salvesen.assignment1;

import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.core.Is.is;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {
    private ConnectionProvider databaseConnection;
    private FileReader fileReader;
    private ArrayList<String> foreignKeysToBeAdded;

    public DatabaseHandlerTest() {
        databaseConnection = new H2DatabaseConnection();
        fileReader = new FileReader();
        foreignKeysToBeAdded = new ArrayList<>();
    }

    @Before
    public void setUp() throws Exception {
        fileReader.setSubjectFile(new File("src/test/Test_table_files/subject_test_file.csv"));
        fileReader.setLecturerFile(new File("src/test/Test_table_files/lecturer_test_file.csv"));
        fileReader.setRoomFile(new File("src/test/Test_table_files/room_test_file.csv"));
    }

    @Test
    public void assertThatDatabaseConnectionIsNotNull() throws SQLException {
        assertNotNull(databaseConnection.getConnection());
    }

    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        //Assert that is up before tearing down, and back up after tearing down?
    }

    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {

    }

    @Test
    public void getArrayListOfTableNames() throws Exception {

    }

    @Test
    public void getRowsFromTableByColumnNameAndSearchColumnValue() throws Exception {
    }

    @Test
    public void getAllRowsByTableName() throws Exception {
    }

}