package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private MysqlDataSource dataSource;

    //// TODO: 19.09.2017 needs to be set in another class? Maybe in a property file?
    public void databaseBuilder() throws SQLException {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");

        /**
         *  TODO: If database exists, print out number if lines in database, ++
         *  and ask if user wants to delete, if user wants to delete,
         *  ask for password, if password is correct, delete?
         **/

        //dataSource.setDatabaseName("westerdals_schedule");
        dataSource.setDatabaseName("pgr200_assignment_1");
        // TODO: Ask user for password or nah?
        dataSource.setUser("pgr200");
        dataSource.setPassword("pgr200");
        createDatabase();
        createSubjectTable();
        System.out.println("Database connected.");
    }

    private void createDatabase() throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
            //stmt.execute("CREATE SCHEMA IF NOT EXISTS woact_schedule;");
            stmt.execute("CREATE SCHEMA IF NOT EXISTS pgr200_assignment_1;");

        }
    }

    private void createSubjectTable() throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS subject (\n" +
                    "id INT(11) AUTO_INCREMENT UNIQUE,\n" +
                    "name varchar(255) UNIQUE NOT NULL,\n" +
                    "attending_students INT(6),\n" +
                    "teaching_form varchar(50) NOT NULL,\n" +
                    "duration FLOAT(11),\n" +
                    "PRIMARY KEY(id));");
        }
    }

    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}
