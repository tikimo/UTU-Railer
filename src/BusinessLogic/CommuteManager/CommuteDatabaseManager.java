package BusinessLogic.CommuteManager;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CommuteDatabaseManager {
    private Connection conn = null;
    private String dbname;

    public CommuteDatabaseManager(String dbname) {
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
            statement.executeUpdate("CREATE TABLE trains(\n" +
                    "  serializedTrain TEXT PRIMARY KEY NOT NULL ,\n" +
                    "  departureTime TEXT NOT NULL ,\n" +
                    "  departureCity TEXT NOT NULL ,\n" +
                    "  arrivalTime   TEXT NOT NULL ,\n" +
                    "  arrivalCity   TEXT NOT NULL\n" +
                    ")");
            statement.close();
            System.err.println("Table '"+ dbname +"' created successfully");

        } catch (SQLException e) {
            if (e.toString().contains("is locked")) {
                System.err.println("[ERROR] Database file "+ dbname +" is locked. Will try to recover...");
            } else if (e.toString().contains("already exists")) {
                System.err.println("[WARNING] SQL error, table " + dbname + " already exists. Moving on...");
            } else {
                e.printStackTrace();
            }
        }
    }

    public boolean addNewTrain(Train train) throws IOException {
        boolean failed = false;

        // Statement
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate("INSERT INTO " + dbname + " (serializedTrain, departureTime, departureCity, arrivalTime, arrivalCity) " +
                    "VALUES ('" + trainToString(train) + "','" + train.getDepartureTime() + "','" + train.getDepartureStation() +
                    "','" + train.getArrivalTime() + "','" + train.getArrivalStation() + "')"
            );
            System.err.println("Train added successfully");
            return !failed;

        } catch (SQLException e) {
            if (e.toString().contains("constraint failed")) {
                System.err.println("[WARNING] Train already exists! Moving on...");
            } else {
                e.printStackTrace();
            }
        }
        return failed;
    }

    public List<Train> getTrainsByProperty(String property, String value) throws IOException, ClassNotFoundException {
        List<Train> trains = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT serializedTrain FROM " + dbname +
                    " WHERE " + property + "='" + value + "'");
            while (rs.next()) {
                trains.add(trainFromString(rs.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Reads train-object from base64 string
     * @param s Train object as a string
     * @return Train object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static Train trainFromString(String s) throws IOException, ClassNotFoundException{
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));

        Train train = (Train) ois.readObject();
        ois.close();
        return train;
    }

    /**
     * Makes serialized Train to base64 string
     * @param o Train object
     * @return String of object
     * @throws IOException
     */
    private static String trainToString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(o);
        oos.close();

        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

}
