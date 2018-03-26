package FrontEnd.Login.adminLogin;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class adminLoginController {
    public JFXButton loginAdminPortalButton;
    public JFXButton loginOrderPortalButton;

    public void initialize() {
        loginAdminPortalButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
        loginOrderPortalButton.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, new CornerRadii(2), new Insets(2))));
    }

    public void loginOrderPortal() {
    }

    public void loginAdminPortal() {
    }
}
