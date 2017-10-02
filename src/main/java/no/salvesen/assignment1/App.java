package no.salvesen.assignment1;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        App app = new App();
        app.runApp();
    }

    /**
     * Currently drops table and recreates, and fills it with information in file due to issues
     * regarding making the entire system generic, and to be valid for all classes and tables.
     * Currently using web-server to make the creation of a login for each tester redundant for now.
     * This makes the app very slow, due to having to reach the web server apparently.
     *
     * @throws FileNotFoundException
     * @throws SQLException
     */
    private void runApp() throws FileNotFoundException, SQLException {
        InputHandler inph = new InputHandler();
        DatabaseHandler dbh = new DatabaseHandler();
        dbh.fillTable(inph.getSubjectFile(), "subject");
        dbh.fillTable(inph.getRoomFile(), "room");
        dbh.fillTable(inph.getLecturerFile(), "lecturer");
        inph.choices();
    }
}
