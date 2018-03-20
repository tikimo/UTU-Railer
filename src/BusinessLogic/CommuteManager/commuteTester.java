package BusinessLogic.CommuteManager;

import BusinessLogic.CommuteManager.Enums.Stations;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class commuteTester extends Application {
    @Override
    public void start(Stage primaryStage)  {

        CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
        // cdm.fillDatabaseWithRandomTrains(cdm, 8);


        // This only fetches trains from Turku and prints data of them
        try {

            // Retrieve trains with a property, throws both exceptions
            ArrayList<Train> trains = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, Stations.TURKU.getCity());

            // Sorting trains by lambda property function
            //  For example departure time ascending is Train::getDepartureTime
            trains.sort(Comparator.comparing(Train::getDepartureTime));

            // Selecting a train
            Train train = trains.get(0);

            // Retrieve cabinet list from train
            ArrayList<Cabinet> cabins = train.getCabinetList();

            // Selecting a cabinet
            Cabinet cabinet = cabins.get(0);

            // Retrieve seats in a cabinet
            ArrayList<Seat> seats = cabinet.getSeatList();

            // Selecting a seat
            Seat seat = seats.get(0);

            // Iterating trains
            for (Train t : trains) {

                // Departure and arrival station retrieving
                System.out.println("Train from " + t.getDepartureStation() + " to " + t.getArrivalStation());

                // You can test matching
                test(t,0,0);

                // You can even print cabins
                t.getCabinetList().get(0).printCabin();
                System.out.println("\n");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }



        System.exit(0);
    }

    private void test(Train train, int cabin, int seat) {
        int seatNumber = train.getCabinetList().get(cabin).getSeatList().get(seat).getSeatNumber();
        String seatType = train.getCabinetList().get(cabin).getSeatList().get(seat).getSeatType();
        System.out.println("Seatnumber is " + seatNumber + " and type is: " + seatType + " on cabin " +
                cabin + " and seat " + seat);
    }
}
