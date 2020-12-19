package hu.unideb.connection;

import hu.unideb.exceptions.MySqlConnectionFailedException;

import java.sql.*;

public class MySQLConnection {
    private static Connection conn;
    private MySQLConnection(){}

    public static Connection GetConnection() throws MySqlConnectionFailedException {
        if(conn == null || !isDbConnected())
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

    private static boolean isDbConnected() {
        final String CHECK_SQL_QUERY = "SELECT 1";
        boolean isConnected = false;
        try {
            final PreparedStatement statement = conn.prepareStatement(CHECK_SQL_QUERY);
            isConnected = true;
        } catch (SQLException | NullPointerException e) {
            // handle SQL error here!
        }
        return isConnected;
    }
}
