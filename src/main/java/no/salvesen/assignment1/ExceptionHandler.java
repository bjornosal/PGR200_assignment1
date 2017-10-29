package no.salvesen.assignment1;

public class ExceptionHandler {

    public void outputIOException() {
        System.out.println("Issues writing properties to file.");
    }

    public void outputFileNotFoundException() {
        System.out.println("Could not find file(s). Please check that file(s) is present.");
    }

    public void outputSQLException() {
        System.out.println("There was an issue creating table with information in the file.");
    }
}
