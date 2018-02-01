package BusinessLogic;

import java.sql.*;

public class DatabaseManager {
    private Connection conn = null;
    private String dbname;

    public DatabaseManager(String dbname) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbname+".db");
            System.err.println("Database connection successful");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection error");
        }
        this.dbname = dbname;
        createTable();
    }

    private void createTable() {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE " + "users" +
                    "(Fname     TEXT    NOT NULL ," +
                    "Lname      TEXT    NOT NULL ," +
                    "email      VARCHAR (30)    PRIMARY KEY NOT NULL ," +
                    "passwd     TEXT    NOT NULL )");
            statement.close();
            System.err.println("Table '"+ "users" +"' created successfully");

        } catch (SQLException e) {
            if (e.toString().contains("is locked")) {
                System.err.println("[ERROR] Database file is locked. Will try to recover...");
            } else if (e.toString().contains("already exists")) {
                System.err.println("[WARNING] SQL error, table " + "users" + " already exists. Moving on...");
            } else {
                e.printStackTrace();
            }
        }
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

    public void addNewUser(String Fname, String Lname, String email, String passwd)  {


        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO users (Fname, Lname, email, passwd)" +
                    "VALUES ('" + Fname + "','" + Lname + "','" + email + "','" + passwd + "') ");
            System.err.println("User added successfully");
        } catch (SQLException e) {
            if (e.toString().contains("constraint failed")) {
                System.err.println("[WARNING] User already exists! Moving on...");
            } else {
                e.printStackTrace();
            }
        }
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
     * Assume user exists and has both names in database.
     * @param email
     * @return
     */
    public String getUserName (String email) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Fname, Lname FROM "+dbname+" WHERE email='"+email+"'");
            return resultSet.getString(1) +" "+ resultSet.getString(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
