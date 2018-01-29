package BusinessLogic;

import java.sql.*;

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
        System.err.println("Table created successfully");
    }

    private void createTable() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE users" +
                    "(Fname     TEXT    NOT NULL ," +
                    "Lname      TEXT    NOT NULL ," +
                    "email      VARCHAR (30)    PRIMARY KEY NOT NULL ," +
                    "passwd     VARCHAR (20)    NOT NULL )");
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewUser(String Fname, String Lname, String email, String passwd) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO users VALUES ('" + Fname + "','" + Lname + "','" + email + "','" + passwd + "') ");
        conn.close();
    }

    public boolean userExists(String email) {
        Statement statement;
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = statement.executeQuery("SELECT email FROM "+dbname+ "where email='" + email + "'");
            return rs.next();   // returns true if email (key value) exists.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns first and last name if passwords matched, else returns null.
     * @param email plaintext email from UI field
     * @param cryptPass SHA256 password from ui
     * @return
     */
    public boolean authenticate(String email, String cryptPass) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT email FROM "+dbname+" WHERE email='"+email+"'AND passwd='"+cryptPass+"'" );
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
