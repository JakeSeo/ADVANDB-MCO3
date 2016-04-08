package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection implements Serializable {
    private static DBConnection instance = null;

    private String driverName;
    private String url;
    private String database;
    private String username;
    private String password;

    /**
     * constructor for connection
     *
     * @param dN driver name
     * @param url URL
     * @param db database to access
     * @param un username
     * @param pw password
     */
    DBConnection() {
        driverName = "com.mysql.jdbc.Driver";
        url = "jdbc:mysql://localhost:3306/";
        database = "db_hpq_palawan";
        username = "root";
        password = "TaylorSwift13";//tempo
    }
     
    /**
     * returns an instance of the Database Connection
     *
     * @return instance of the Database Connection
     */
    /**
     * returns a connection to database
     *
     * @return connection to database
     */
    public Connection getConnection() {

        try {
            return DriverManager.getConnection(getUrl()
                    + getDatabase(),
                    getUsername(),
                    getPassword());
        } catch (SQLException se) {
            se.printStackTrace();
        }

        return null;
    }

    /**
     * returns database URL
     *
     * @return database URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * returns database name
     *
     * @return database name
     */
    public String getDatabase() {
        return database;
    }

    /**
     * returns username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

    /**
     * returns whether password is correct or not
     *
     * @param password password to checkPassword
     * @return whether password is correct or not
     */
    public boolean isCorrectPassword(String password) {
        return password.equals(this.password);
    }
}
