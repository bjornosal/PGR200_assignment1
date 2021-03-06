package no.salvesen.assignment1;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FileReaderTest {

    private FileReader fileReader = new FileReader();

    @Before
    public void setUp() throws Exception {
        fileReader.setSubjectFile(new File("src/test/Test_table_files/subject_test_file.csv"));
        fileReader.setLecturerFile(new File("src/test/Test_table_files/lecturer_test_file.csv"));
        fileReader.setLecturer_in_subject_file(new File("src/test/Test_table_files/lecturer_in_subject_test_file.csv"));
        fileReader.setRoomFile(new File("src/test/Test_table_files/room_test_file.csv"));
    }


    /**
     * Asserts that the readFile method works as intended
     * by checking that all fields is the correct size according to the file.
     * @throws Exception
     */
    @Test
    public void readFile() throws Exception {
        fileReader.readFile(fileReader.getFileByTableName("subject"));
        assertEquals(fileReader.getTableColumnCount(), 5);
        assertThat(fileReader.getColumnNames().size(), is(5));
        //Column names and primary key
        assertThat(fileReader.getColumnSQLValues().size(), is(6));
        assertThat(fileReader.getInsertionValues().size(), is(45));
    }

    @Test
    public void getFileByTableName() throws Exception {
        File initializedToNull = fileReader.getFileByTableName("");
        assertNull(initializedToNull);

        File subjectFile = fileReader.getFileByTableName("subject");
        assertEquals(subjectFile.getPath(), "src\\test\\Test_table_files\\subject_test_file.csv");
    }
}