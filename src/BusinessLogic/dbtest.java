package BusinessLogic;

import javafx.application.Application;
import javafx.stage.Stage;

public class dbtest extends Application {
    private DatabaseManager dbm;

    @Override
    public void start(Stage primaryStage) throws Exception {
        dbm = new DatabaseManager("users");
    }
}
