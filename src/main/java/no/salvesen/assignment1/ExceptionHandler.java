package no.salvesen.assignment1;

public class ExceptionHandler {

    public void outputIOException(String location) {
        switch(location) {
            case "WriteProp":
                System.out.println("Issues writing properties to file.");
                break;
            case "connect":
                System.out.println("There was an issue with the database name");
                break;
        }
    }

    public void outputFileNotFoundException() {
        System.out.println("Could not find file(s). Please check that file(s) is present.");
    }

    public void outputSQLException(String issue) {
        switch(issue) {
            case "connect":
                System.out.println("There was an issue connecting to the database with the properties-file. \nPlease try again.");
                break;
            case "createTable":
                System.out.println("There was an issue creating table with information in the file.");
                break;
        }
    }
}
