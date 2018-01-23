package BusinessLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private Connection conn = null;
    private String dbname = null;

    public DatabaseManager(String dbname) {
        this.dbname = dbname;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbname+".db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection error");
        }
        System.err.println("Database connection successfull");
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE users" +
                    "(Fname     TEXT    NOT NULL " +
                    "Lname      TEXT    NOT NULL " +
                    "email      VARCHAR (30)    PRIMARY KEY NOT NULL " +
                    "passwd     VARCHAR (20)    NOT NULL )");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewUser(String Fname, String Lname, String email, String passwd) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO users");
        conn.close();
    }
}
