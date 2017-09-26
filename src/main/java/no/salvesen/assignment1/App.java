package no.salvesen.assignment1;

import java.sql.SQLException;

public class App
{
    public static void main(String[] args) throws SQLException {
       // DatabaseConnector dbc = new DatabaseConnector();
     //   dbc.databaseBuilder();
        InputHandler inph = new InputHandler();
        inph.fileFinder();
    }
}
