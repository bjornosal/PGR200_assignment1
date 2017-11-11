package no.salvesen.assignment1;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;


import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class InputHandlerTest {

    DatabaseHandler databaseHandler = new DatabaseHandler();

    @Before
    public void setUp() {
        String simulatedUserInput = "1" + System.getProperty("line.separator") + "1" +
                System.getProperty("line.separator") + "1" + System.getProperty("line.separator") +
                "pgr200" + System.getProperty("line.separator") + "2" + System.getProperty("line.separator")
                + "3" + System.getProperty("line.separator") + "Per Lauvas" + System.getProperty("line.separator")
                + "4" + System.getProperty("line.separator") + "5" + System.getProperty("line.separator")
                + "F101" + System.getProperty("line.separator") + "6" + System.getProperty("line.separator")
                + "7" + System.getProperty("line.separator") + "8" + System.getProperty("line.separator")
                + "2" + System.getProperty("line.separator") + "1" + "filepath"+System.getProperty("line.separator")
                + "2" + System.getProperty("line.separator") + "filepath" + System.getProperty("line.separator")
                + "filepath" + System.getProperty("line.separator") + "3" + System.getProperty("line.separator")
                + "filepath" + System.getProperty("line.separator") + "4" + System.getProperty("line.separator")
                + "5" + System.getProperty("line.separator") + "6" + System.getProperty("line.separator")
                + "room" + System.getProperty("line.separator") + "6" + System.getProperty("line.separator")
                + "subject" + System.getProperty("line.separator") + "Y" + System.getProperty("line.separator")
                + "8";

        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        System.setIn(in);
    }

    @Test
    public void startMenuLoop() throws Exception {
//   Source:https://stackoverflow.com/questions/23653875/how-to-simulate-multiple-user-input-for-junit

        InputHandler inputHandler = new InputHandler();
        inputHandler.startMenuLoop();
    }

    @After
    public void tearDown() {
        System.setIn(System.in);
    }
}