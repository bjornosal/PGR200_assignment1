package no.salvesen.assignment1;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        App app = new App();
        app.runApp();
    }


    private void runApp() throws FileNotFoundException, SQLException {
        InputHandler inph = new InputHandler();
        inph.fileFinder();
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.findColumnCount("subject");
        dbh.fillTable(inph.getSubjectFile(), "subject");
    }


}
