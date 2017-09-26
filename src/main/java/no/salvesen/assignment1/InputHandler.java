package no.salvesen.assignment1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputHandler {

    //TODO Method for checking that file is correct format
    //TODO Method for parsing file

    public void inputReader() {
        try {
            Scanner input = new Scanner(new File("C:/IdeaProjects/assignment1/src/files"));
            input.useDelimiter(";");

            while(input.hasNext()) {
                System.out.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
