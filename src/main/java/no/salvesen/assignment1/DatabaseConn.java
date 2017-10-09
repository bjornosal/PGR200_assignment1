package no.salvesen.assignment1;


import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConn {

    private MysqlDataSource dataSource;

    public DatabaseConn() throws SQLException {
        databaseBuilder();
    }

    public void databaseBuilder() throws SQLException {
        dataSource = new MysqlDataSource();

        /**
         * Login to webserver.
         * If local server wantet, comment out next four lines and
         * remove comment on next lines. Fill in proper information.
         */ /*
        dataSource.setServerName("tek.westerdals.no");
        dataSource.setDatabaseName("salbjo16_pgr200_assignment1");
        dataSource.setUser("salbjo16_pgr200");
        dataSource.setPassword("pgr200!");
*/

        /*
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName("pgr200_assignment_1");
        dataSource.setUser("pgr200");
        dataSource.setPassword("pgr200");
        */

    }


    public MysqlDataSource getDataSource() {
        return dataSource;
    }
}
