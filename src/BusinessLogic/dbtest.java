package BusinessLogic;

import javafx.application.Application;
import javafx.stage.Stage;

public class dbtest extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager dbm = new DatabaseManager("users");
        dbm.addNewUser("Asad", "Ijaz", "asaija@utu.fi", "asadin salasana");
        System.out.println(dbm.getUserName("asaija@utu.fi"));

        System.out.println(dbm.authenticate("asaija@utu.fi","asadin salasana ei ole tämä"));
    }
}
