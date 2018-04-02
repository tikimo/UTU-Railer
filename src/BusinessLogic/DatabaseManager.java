package BusinessLogic;

import java.sql.*;

/**
 * This class is a database manager just like CommuteDAtabaseManager.
 * This databasemanager only handles user information. Uses the Crypter
 * class for sotring passwords.
 *
 * In database, key value of each user is email so no duplicates
 */
public class DatabaseManager {
    private Connection conn = null;
    private String dbname;
    private Crypter crypter = new Crypter();

    /**
     * Constructor
     * @param dbname database address to connect to.
     */
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

    /**
     * This method is invoked whenever constructor is called.
     * Checks if table exists in db, if not create it. Otherwise continue...
     */
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
                    "  cardnum   TEXT    ,\n" +
                    "  admin     INTEGER )");
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
     * Checks if user and passwords match. returns a boolean of success.
     *
     * @param email plaintext email from UI field
     * @param plainPass plaintext password from UI: crypting moved from front to back
     * @return Returns true if passwords matched, else returns false.
     */
    public boolean authenticate(String email, String plainPass) {
        boolean success;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT email FROM "+dbname+" WHERE email='"+email+"'AND passwd='"+crypter.generateHash(plainPass)+"'" );
            success = rs.next();
            if (success) {
                System.err.println("Database authentication confirmed.");
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Simply adds a new user to database. Make sure none of the fields are empty. This
     * is front-end's reponsibility.
     *
     * @param Fname Front name
     * @param Lname Last name
     * @param email email address to associate account with (login name)
     * @param plainPass desired passwords as plaintext
     */
    public void addNewUser(String Fname, String Lname, String email, String plainPass)  {


        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO users (Fname, Lname, email, passwd) " +
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

    /**
     * Checks if user exists. This should be called before authentication for obvious reasons.
     * @param email email (login id) of user.
     * @return returns a boolean of success
     */
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

    /**
     * Updates user's password. This should only be done after authentication. And
     * that is also front-end's responsibility.
     *
     * @param plainPass password as plaintext
     * @param email email (user id) to have password changed on
     * @return returns boolean of success
     */
    public boolean updatePassword(String plainPass, String email) {
        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE users " +
                    "SET passwd = '" + crypter.generateHash(plainPass) + "'" +
                    "WHERE email = '" + email + "'");
            System.err.println("Password updated successfully!");
            return true;
        } catch (SQLException sqle) {
            System.err.println("SQL Error! Code: " + sqle.getErrorCode());
        }
        return false;
    }

    /**
     * Adds address to user
     * @param address address as string. Format is front-end responsibility
     * @param email account that address should be associated with
     * @return returns boolean of success
     */
    public boolean addAddress (String address, String email) {
        boolean success = false;
        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("UPDATE users " +
                    "SET address = '" + address + "' WHERE email='" + email + "'");
            System.err.println("Address updated successfully");
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
     * Adds phone number to user
     * @param phone number to be attached. Format is front-end's reponsibility
     * @param email account that phone no. should be associated with
     * @return boolean of success
     */
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

    /**
     * Adds a credit card to user
     * @param card CC number. Format is again front-end's responsibility
     * @param email account that CC should be associated with
     * @return boolean of success
     */
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
     * Returns the user's Fname and Lname
     * Assume user exists and has both names in database.
     * @param email Users email address
     * @return the name retrieved from database
     */
    public String getUserName (String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Fname, Lname FROM "+dbname+" WHERE email='"+email+"'");
            if (resultSet.next()) {
                returnable = resultSet.getString(1) +" "+ resultSet.getString(2);
            } else {
                System.err.println("[FATAL] No name record found on user!");
            }
            return returnable;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the user's address
     * Assume user exists and has address in database.
     * @param email Users email address
     * @return the address retrieved from database
     */
    public String getAddress(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT address FROM "+dbname+" WHERE email='"+email+"'");
            if (resultSet.next()) {
                returnable = resultSet.getString(1);
            } else {
                System.err.println("[WARNING] No address record found on user!");
            }
            return (returnable == null) ? "NaN" : returnable;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }

    /**
     * Returns the user's phone
     * Assume user exists and has phone number in database.
     * @param email Users email address
     * @return the number retrieved from database
     */
    public String getPhone(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT phone FROM "+dbname+" WHERE email='"+email+"'");
            if (resultSet.next()) {
                returnable = resultSet.getString(1);
            } else {
                System.err.println("[WARNING] No phone record found on user!");
            }
            return (returnable == null) ? "NaN" : returnable;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }

    /**
     * Returns the user's credit card number
     * Assume user exists and has credit card saved in database.
     * @param email Users email address
     * @return the CC retrieved from database
     */
    public String getCard(String email) {
        String returnable = "NaN";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cardnum FROM "+dbname+" WHERE email='"+email+"'");
            if (resultSet.next()) {
            returnable = resultSet.getString(1);
            } else {
                System.err.println("[WARNING] No card record found on user!");
            }
            return (returnable == null) ? "NaN" : returnable;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnable;
    }

    /**
     * Checks if user is admin. User is admin if the field "admin" in database is 1
     * @param email account to run check on
     * @return boolean of success
     */
    public boolean isAdmin(String email) {
        Statement statement;
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT admin FROM " + dbname + " WHERE email='" + email + "'");
            if (rs.getInt(1) == 1) {
                System.err.println("User is admin, database checked.");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}


