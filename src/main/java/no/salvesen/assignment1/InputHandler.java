package no.salvesen.assignment1;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputHandler {

    //TODO Method for checking that file is correct format
    //TODO Method for parsing file
    private File subjectFile;
    private File roomFile;
    private File lecturerFile;
    private int tablePick;
    private String tableName;
    private String filePath;

    public void fileFinder() {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please choose which table you are entering information for: ");
        System.out.println("1: Subject");
        System.out.println("2: Room");
        System.out.println("3: Lecturer");
        tablePick = userInput.nextInt();

        switch(tablePick) {
            case 1:
                tableName = "subject";
                break;
            case 2:
                tableName = "room";
                break;
            case 3:
                tableName = "lecturer";
                break;
            default:
                tableName = "subject";
                break;

        }
        System.out.println("Please copy the file-path to the csv file for " + tableName + "s.");
        userInput.nextLine();
        //TODO: Test required to check that file is actually a file
        filePath = userInput.nextLine();
        switch(tablePick) {
            case 1:
                subjectFile = new File(filePath);
                break;
            case 2:
                roomFile = new File(filePath);
                break;
            case 3:
                lecturerFile = new File(filePath);
                break;
        }
        System.out.println(filePath);
    }


}