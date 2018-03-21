package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.SeatTypes;
import BusinessLogic.CommuteManager.Enums.Stations;

import java.io.*;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CommuteDatabaseManager {
    // Properties for querying train info
    public final String SERIALIZED_TRAIN = "serializedTrain";
    public final String DEPARTURE_TIME = "departureTime";
    public final String DEPARTURE_CITY = "departureCity";
    public final String ARRIVAL_TIME = "arrivalTime";
    public final String ARRIVAL_CITY = "arrivalCity";

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

    boolean addNewTrain(Train train) {
        boolean failed = false;
        if (this.dbname == null) {
            this.dbname = "trains";
        }

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

    public ArrayList<Train> getTrainsByProperty(String property, String value) throws IOException, ClassNotFoundException {
        ArrayList<Train> trains = new ArrayList<>();
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
        return trains;

    }

    /**
     * Reads train-object from base64 string
     * @param s Train object as a string
     * @return Train object
     * @throws IOException IOException for object streams
     * @throws ClassNotFoundException   Base64 decoder may throw this
     */
    static Train trainFromString(String s) throws IOException, ClassNotFoundException{
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
    static String trainToString(Serializable o) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    static Train generateRandomTrain() {

        // Generate random cabinets with random seat types
        ArrayList<Cabinet> cabinetList = new ArrayList<>();
        for (int i = 0; i<5; i++) {
            cabinetList.add(generateRandomCabinet());
        }

        String departureStation = Stations.getRandomStation().getCity();
        String arrivalStation = Stations.getRandomStation().getCity();

        while (departureStation.equals(arrivalStation)) {
            arrivalStation = Stations.getRandomStation().getCity();
        }


        // Set random times
        int startHour = ThreadLocalRandom.current().nextInt(0, 24);
        int startMin = ThreadLocalRandom.current().nextInt(0, 60);
        int stopHour = ThreadLocalRandom.current().nextInt(startHour, 24);
        int stopMin = ThreadLocalRandom.current().nextInt(0, 60);
        LocalTime departureTime = LocalTime.of(startHour, startMin);
        LocalTime arrivalTime = LocalTime.of(stopHour, stopMin);

        Train randomTrain = new Train(departureStation, arrivalStation, departureTime, arrivalTime);
        randomTrain.setCabinetList(cabinetList);

        return randomTrain;
    }

    private static Cabinet generateRandomCabinet() {
        Random random = new Random();
        ArrayList<Seat> seatList = new ArrayList<>();
        for (int i = 1; i<=60; i++) {
            Seat seat = new Seat(i, SeatTypes.getRandomSeatType().name().toLowerCase());
            seat.setReserved(random.nextBoolean());
            seatList.add(seat);
        }
        Cabinet randomCabinet = new Cabinet(seatList);
        // randomCabinet.setSeatList(seatList);
        return randomCabinet;
    }

    public void fillDatabaseWithRandomTrains(CommuteDatabaseManager cdm, int count) {
        for (int i = 0; i<count; i++) {
            cdm.addNewTrain(generateRandomTrain());
        }
    }
}
