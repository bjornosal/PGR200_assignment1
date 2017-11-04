package no.salvesen.assignment1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2DatabaseConnection implements ConnectionProvider {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test";
    private static final String DB_USER = "test";
    private static final String DB_PASSWORD = "";

    @Override
    public void setPropertiesForDatabase(String propertyFilePath) {

    }

    @Override
    public Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Unable to find H2 driver");
        }

        return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
    }
}
