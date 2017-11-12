package no.salvesen.assignment1;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.Rule;


import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

public class InputHandlerTest {

    DatabaseHandler databaseHandler = new DatabaseHandler();
    private final String lineBreak = System.getProperty("line.separator");
    private final String userInputOnPropertiesMenu = "3" + lineBreak + "localhost" + lineBreak + "";


    @Before
    public void setUp() {
        String simulatedUserInput = "1" + lineBreak + "1" +
                lineBreak + "1" + lineBreak +
                "pgr200" + lineBreak + "2" + lineBreak
                + "3" + lineBreak + "Per Lauvas" + lineBreak
                + "4" + lineBreak + "5" + lineBreak
                + "F101" + lineBreak + "6" + lineBreak
                + "7" + lineBreak + "8" + lineBreak
                + "2" + lineBreak + "1" + "filepath"+lineBreak
                + "2" + lineBreak + "filepath" + lineBreak
                + "filepath" + lineBreak + "3" + lineBreak
                + "filepath" + lineBreak + "4" + lineBreak
                + "5" + lineBreak + "6" + lineBreak
                + "room" + lineBreak + "6" + lineBreak
                + "subject" + lineBreak + "Y" + lineBreak
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