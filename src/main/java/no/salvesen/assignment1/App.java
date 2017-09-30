package no.salvesen.assignment1;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class App
{
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        InputHandler inph = new InputHandler();

        DatabaseHandler dbh = new DatabaseHandler();
        dbh.findColumnCount("subject");
    }
}
