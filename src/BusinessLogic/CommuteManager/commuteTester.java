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
    public void start(Stage primaryStage) throws Exception {

        CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
        // cdm.fillDatabaseWithRandomTrains(cdm, 8);

        Train train = CommuteDatabaseManager.generateRandomTrain();

        train.setDepartureStation(Stations.TURKU.getCity());
        train.setArrivalStation(Stations.HELSINKI.getCity());

        train.setDepartureTime(LocalTime.of(15,30));
        train.setArrivalTime(LocalTime.of(17, 0));

        cdm.addNewTrain(train);

        ArrayList<Train> trains = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, Stations.TURKU.getCity());

        trains.sort(Comparator.comparing(Train::getDepartureTime));

        for (Train t : trains) {
            System.out.println(t.getDepartureStation() + t.getDepartureTime() + t.getArrivalStation() + t.getArrivalTime());
        }

        System.exit(0);
    }

    private void test(Train train) {
        int seatNumber = train.getCabinetList().get(2).getSeatList().get(2).getSeatNumber();
        String seatType = train.getCabinetList().get(2).getSeatList().get(2).getSeatType();
        System.out.println("Seatnumber is " + seatNumber + " and type is: " + seatType);
    }
}
