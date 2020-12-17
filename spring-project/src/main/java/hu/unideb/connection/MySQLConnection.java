package hu.unideb.connection;

import hu.unideb.exceptions.MySqlConnectionFailedException;

import java.sql.*;

public class MySQLConnection {
    private static Connection conn;
    private MySQLConnection(){}

    public static Connection GetConnection() throws MySqlConnectionFailedException {
        if(conn == null)
        {
            try {
                conn = DriverManager.getConnection("jdbc:mysql://remotemysql.com/kykbkt8Ush", "kykbkt8Ush", "SuJJ69EaOO");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                throw new MySqlConnectionFailedException("Connection to MySql database failed.");
            }
        }
        return conn;
    }
}
