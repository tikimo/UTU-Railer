package BusinessLogic;

import java.sql.*;

public class DatabaseManager {
    private Connection conn = null;
    private String dbname;

    public DatabaseManager(String dbname) {
        this.dbname = dbname;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:"+dbname+".db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection error");
        }
        System.err.println("Database connection successful");
        createTable("users");
    }

    private void createTable(String tableName) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE " + tableName +
                    "(Fname     TEXT    NOT NULL ," +
                    "Lname      TEXT    NOT NULL ," +
                    "email      VARCHAR (30)    PRIMARY KEY NOT NULL ," +
                    "passwd     VARCHAR (20)    NOT NULL )");
            statement.close();
            System.err.println("Table '"+tableName+"' created successfully");

        } catch (SQLException e) {
            if (e.toString().contains("already exists")) {
                System.err.println("[WARNING] SQL error, table "+tableName+" already exists. Moving on...");
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

    public void addNewUser(String Fname, String Lname, String email, String passwd) throws SQLException {


        // Statement
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
     * Assume user exists and has both names in database.
     * @param email
     * @return
     */
    public String getUserName (String email) {
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Fname, Lname FROM "+dbname+" WHERE email='"+email+"'");
            return resultSet.getString(0) +" "+ resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
