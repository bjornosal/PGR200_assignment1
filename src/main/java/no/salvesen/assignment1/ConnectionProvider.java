package no.salvesen.assignment1;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider{
    public Connection getConnection() throws SQLException;
}
