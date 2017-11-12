package no.salvesen.assignment1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.core.Is.is;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DatabaseHandlerTest {
    private DatabaseHandler databaseHandler;
    private final String subjectFilePathName = "src/test/Test_table_files/subject_test_file.csv";
    private final String lecturerFilePathName = "src/test/Test_table_files/lecturer_test_file.csv";
    private final String roomFilePathName = "src/test/Test_table_files/room_test_file.csv";
    private final String lecturerInSubjectPathName = "src/test/Test_table_files/lecturer_in_subject_test_file.csv";

    public DatabaseHandlerTest() throws IOException, SQLException {
        databaseHandler = new DatabaseHandler(subjectFilePathName, roomFilePathName,lecturerFilePathName, lecturerInSubjectPathName);
    }

    @Before
    public void setUp() throws Exception {
        databaseHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
        databaseHandler.startConnection();
    }

    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {
        tearDownDatabaseAndSetBackUp();
        databaseHandler.tearDownTableAndSetBackUpWithNewInformation("room");
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @After
    public void tearDown() throws Exception {
        databaseHandler.dropDatabase();
    }
}