package FrontEnd.Portal;

import FrontEnd.Login.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

public class portalTester extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginController lgc = new LoginController();
        lgc.launchPortal("tikimo@utu.fi", true );

    }
}
