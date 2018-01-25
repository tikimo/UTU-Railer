package FrontEnd.Login;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {
    public Label createAccountLink;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public PasswordField passwordField;

    public void showAccountCreationDialog(MouseEvent mouseEvent) {
        firstNameField.setDisable(false);
    }
}
