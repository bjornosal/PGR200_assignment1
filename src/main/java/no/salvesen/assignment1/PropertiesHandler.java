package no.salvesen.assignment1;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The type Properties handler.
 */
public class PropertiesHandler {

    private String propertyFilePath;

    /**
     * Instantiates a new Properties handler.
     */
    public PropertiesHandler() {
    }

    /**
     * Initialize properties.
     *
     * @param dataSource the data source
     * @throws IOException the io exception
     */
    public void assignPropertiesToDatasource(MysqlDataSource dataSource) throws IOException {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(propertyFilePath)) {

            properties.load(input);

            dataSource.setServerName(properties.getProperty("serverName"));
            dataSource.setUser(properties.getProperty("databaseUser"));
            dataSource.setPassword(properties.getProperty("databasePassword"));
        }
    }

    /**
     * Gets database name from properties.
     *
     * @return the database name from properties
     * @throws IOException the io exception
     */
    public String getDatabaseNameFromProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertyFilePath)) {
            properties.load(input);
            return properties.getProperty("databaseName");
        }
    }

    /**
     * Sets database name in properties.
     *
     * @param databaseName the database name
     * @throws IOException the io exception
     */
    protected void setDatabaseNameInProperties(String databaseName) throws IOException {
        Properties properties = new Properties();
        String currentServerName;
        String currentUser;
        String currentPassword;

        try(InputStream input = new FileInputStream(propertyFilePath)) {
            properties.load(input);
            currentServerName = properties.getProperty("serverName");
            currentUser = properties.getProperty("databaseUser");
            currentPassword = properties.getProperty("databasePassword");
        }

        try(InputStream input = new FileInputStream(propertyFilePath);
            FileOutputStream fileOut = new FileOutputStream(propertyFilePath)) {
            properties.load(input);

            properties.setProperty("databaseName", databaseName);
            properties.setProperty("serverName", currentServerName);
            properties.setProperty("databaseUser",currentUser);
            properties.setProperty("databasePassword",currentPassword);

            properties.store(fileOut, "Added by user.");
        }
    }

    /**
     * Gets property file path.
     *
     * @return the property file path
     */
    public String getPropertyFilePath() {
        return propertyFilePath;
    }

    /**
     * Sets property file path.
     *
     * @param propertyFilePath the property file path
     */
    public void setPropertyFilePath(String propertyFilePath) {
        this.propertyFilePath = propertyFilePath;
    }
}
