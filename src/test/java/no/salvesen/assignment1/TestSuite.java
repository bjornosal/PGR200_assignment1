package no.salvesen.assignment1;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DatabaseConnectionTest.class,
        DatabaseHandlerTest.class,
        FileReaderTest.class,
        InputHandlerTest.class
})

/**
 * Used to run all tests in all test classes.
 */
public class TestSuite {

}
