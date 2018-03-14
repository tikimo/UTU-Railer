package BusinessLogic.CommuteManager;

import javafx.application.Application;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.util.ArrayList;

public class commuteTester extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        CommuteDatabaseManager cdm = new CommuteDatabaseManager("trains");
        LocalTime departure = LocalTime.of(22,15);


        Train train = Train.generateRandomTrain();




    }
}
