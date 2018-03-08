package BusinessLogic;

import java.sql.*;

public class DatabaseManager {
    private Connection conn = null;
    private String dbname;
    private Crypter crypter = new Crypter();

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
            statement.executeUpdate("CREATE TABLE users(\n" +
                    "  Fname     TEXT    NOT NULL ,\n" +
                    "  Lname     TEXT    NOT NULL ,\n" +
                    "  email     VARCHAR (30)    PRIMARY KEY NOT NULL ,\n" +
                    "  passwd    TEXT    ,\n" +
                    "  address   TEXT    ,\n" +
                    "  phone     TEXT    ,\n" +
                    "  cardnum   TEXT    )");
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
     * @param plainPass plaintext password from UI: crypting moved from front to back
     * @return
     */
    public boolean authenticate(String email, String plainPass) {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT email FROM "+dbname+" WHERE email='"+email+"'AND passwd='"+crypter.generateHash(plainPass)+"'" );
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addNewUser(String Fname, String Lname, String email, String plainPass)  {


        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO users (Fname, Lname, email, passwd)" +
                    "VALUES ('" + Fname + "','" + Lname + "','" + email + "','" + crypter.generateHash(plainPass) + "') ");
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
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT email FROM "+dbname+ " WHERE email='" + email + "'");
            return rs.next();   // returns true if email (key value) exists.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addAddress (String address, String email) {
        boolean success = false;
        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE users " +
                    "SET address = '" + address + "' WHERE email='" + email + "'");
            System.err.println("Email updated successfully");
            success = true;
        } catch (SQLException e) {
            if (e.toString().contains("constraint failed")) {
                System.err.println("[ERROR] Constraint failed! Error code:");
                e.getErrorCode();
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }
    public boolean addPhone (String phone, String email) {
        boolean success = false;
        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE users " +
                    "SET phone = '" + phone + "' WHERE email='" + email + "'");
            System.err.println("Phone updated successfully");
            success = true;
        } catch (SQLException e) {
            if (e.toString().contains("constraint failed")) {
                System.err.println("[ERROR] Constraint failed! Error code:");
                e.getErrorCode();
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }
    public boolean addCard (String card, String email) {
        boolean success = false;
        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE users " +
                    "SET cardnum = '" + card + "' WHERE email='" + email + "'");
            System.err.println("Card updated successfully");
            success = true;
        } catch (SQLException e) {
            if (e.toString().contains("constraint failed")) {
                System.err.println("[ERROR] Constraint failed! Error code:");
                e.getErrorCode();
            } else {
                e.printStackTrace();
            }
        }
        return success;
    }

    /**
     * Assume user exists and has both names in database.
     * @param email Users email address
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

    public String getAddress(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT address FROM "+dbname+" WHERE email="+email+"'");
            returnable = resultSet.getString(1);
            return returnable.length() > 0 ? returnable : "NaN";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }
    public String getPhone(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT phone FROM "+dbname+" WHERE email="+email+"'");
            returnable = resultSet.getString(1);
            return returnable.length() > 0 ? returnable : "NaN";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }
    public String getCard(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cardnum FROM "+dbname+" WHERE email="+email+"'");
            returnable = resultSet.getString(1);
            return returnable.length() > 0 ? returnable : "NaN";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }

}


