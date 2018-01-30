package BusinessLogic;

import javafx.application.Application;
import javafx.stage.Stage;

public class dbtest extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager dbm = new DatabaseManager("users");
        dbm.addNewUser("Maija", "Meikäläinen", "maikkiiiis@utu.fi", "supersalainen");
        System.out.println(dbm.getUserName("maikkis@utu.fi"));

        System.out.println(dbm.authenticate("maikkis@utu.fi", "huippusalainen"));
        System.out.println(dbm.authenticate("maikkis@utu.fi", "supersalainen"));
    }
}
