package BusinessLogic.CommuteManager;

import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

public class commuteTester extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
        Train train = CommuteDatabaseManager.generateRandomTrain();
        test(train);

        cdm.addNewTrain(train);

        Train secondTrain = cdm.getTrainsByProperty(cdm.DEPARTURE_CITY, train.getDepartureStation()).get(0);

        test(secondTrain);


        System.exit(0);
    }

    private void test(Train train) {
        int seatNumber = train.getCabinetList().get(2).getSeatList().get(2).getSeatNumber();
        String seatType = train.getCabinetList().get(2).getSeatList().get(2).getSeatType();
        System.out.println("Seatnumber is " + seatNumber + " and type is: " + seatType);
    }
}
