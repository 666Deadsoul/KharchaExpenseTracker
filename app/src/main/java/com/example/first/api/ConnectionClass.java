package com.example.first.api;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    protected static String db = "kharcha";
    protected static String ip = "localhost"; //10.0.2.2
    protected static String port = "3306";
    protected static String username = "root";
    protected static String password = "";

    public Connection conn() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + db;
            conn = DriverManager.getConnection(connectionString, username, password);
        } catch (Exception e) {
            Log.e("Error", "Connection error: ", e);
        }
        return conn;
    }
}