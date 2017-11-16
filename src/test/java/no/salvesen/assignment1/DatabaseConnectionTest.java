package no.salvesen.assignment1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseConnectionTest {

    PropertiesHandler propertiesHandler;
    DatabaseConnection databaseConnection;

    @Before
    public void setUp() throws Exception {
        propertiesHandler = new PropertiesHandler();
        propertiesHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
        databaseConnection = new DatabaseConnection(propertiesHandler);
        databaseConnection.initializeProperties();
    }

    @Test
    public void initializeProperties() throws Exception {

    }

    @Test
    public void getConnection() throws Exception {
        assertNotNull(databaseConnection.getConnection());
    }

    @Test
    public void setDataSourceDatabaseName() throws Exception {

    }

}