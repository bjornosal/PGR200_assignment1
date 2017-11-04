package no.salvesen.assignment1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider{
    void setPropertiesForDatabase(String propertyFilePath) throws IOException;
    Connection getConnection() throws SQLException;
}
