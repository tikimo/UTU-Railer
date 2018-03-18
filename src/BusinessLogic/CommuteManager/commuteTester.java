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
        cdm.fillDatabaseWithRandomTrains(cdm, 8);

        try {

            ArrayList<Train> trains = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, Stations.TURKU.getCity());
            System.err.println(trains == null);
            Train train = trains.get(0);

            ArrayList<Cabinet> cabins = train.getCabinetList();
            Cabinet cabinet = cabins.get(0);

            ArrayList<Seat> seats = cabinet.getSeatList();
            System.err.println(seats == null);
            Seat seat = seats.get(0);
            System.out.println(seat.getSeatType());

            for (Train t : trains) {
                System.out.println("Train from " + t.getDepartureStation() + " to " + t.getArrivalStation());
                t.getCabinetList().get(0).printCabin();
                System.out.println("\n");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }




        // cdm.fillDatabaseWithRandomTrains(cdm, 8);

        /*
        Train train = CommuteDatabaseManager.generateRandomTrain();

        train.setDepartureStation(Stations.TURKU.getCity());
        train.setArrivalStation(Stations.HELSINKI.getCity());

        train.setDepartureTime(LocalTime.of(15,30));
        train.setArrivalTime(LocalTime.of(17, 0));



        ArrayList<Train> trains = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, Stations.TURKU.getCity());
        System.out.println("Success: " + trains.get(0).getCabinetList().get(0).getSeatList());

        trains.sort(Comparator.comparing(Train::getDepartureTime));

        for (Train t : trains) {
            System.out.println("\n");
            System.out.println(t.getDepartureStation() + t.getDepartureTime() + t.getArrivalStation() + t.getArrivalTime());
            t.getCabinetList().get(2).printCabin();
        }
        */

        System.exit(0);
    }

    private void test(Train train) {
        int seatNumber = train.getCabinetList().get(2).getSeatList().get(2).getSeatNumber();
        String seatType = train.getCabinetList().get(2).getSeatList().get(2).getSeatType();
        System.out.println("Seatnumber is " + seatNumber + " and type is: " + seatType);
    }
}
