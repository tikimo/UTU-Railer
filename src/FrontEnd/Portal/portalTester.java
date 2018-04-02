package FrontEnd.Portal;

import FrontEnd.Login.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Here you an test the portal functionality with custom user. In this example the user is "tikimo@utu.fi"
 */
public class portalTester extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginController lgc = new LoginController();
        lgc.setAuthenticatedUser("tikimo@utu.fi");
        lgc.launchPortal("tikimo@utu.fi", true );
    }
}
