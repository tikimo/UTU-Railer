package FrontEnd.Login.adminLogin;

import FrontEnd.Login.LoginController;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class adminLoginController {
    public JFXButton loginAdminPortalButton;
    public JFXButton loginOrderPortalButton;
    private String user = null;

    public void initialize() {
        loginAdminPortalButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
        loginOrderPortalButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
    }

    public void loginOrderPortal() {
        LoginController lgc = new LoginController();

        user = LoginController.getAuthenticatedUser();
        lgc.launchPortal(user, false);
    }

    public void loginAdminPortal() {
        try {
            // Setup new stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../Admin/admin.fxml"));
            Parent portalRoot = fxmlLoader.load();
            Stage portalStage = new Stage();
            portalStage.setTitle("U-R admin: " + user);
            portalStage.setScene(new Scene(portalRoot));

            // Set variables and parameters
            portalStage.setResizable(false);
            portalStage.setHeight(768);
            portalStage.setWidth(1024);
            portalStage.getIcons().add(new Image("FrontEnd/RES/logo.png"));

            // Hide login and launch portal
            Stage loginStage = (Stage) loginOrderPortalButton.getScene().getWindow();
            loginStage.hide();

            portalStage.show();

        } catch (IOException e) {
            System.err.println("[ERROR] Cant open new window!");
            e.printStackTrace();
        }

    }
}
