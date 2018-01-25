package FrontEnd.Login;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class LoginController {
    public Label createAccountLink;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public TextField emailFieldLogin;
    public PasswordField passwordFieldLogin;
    public Button signInButton;
    public Button createAccountButton;

    public void showAccountCreationDialog(MouseEvent mouseEvent) {
        firstNameField.setDisable(false);
        lastNameField.setDisable(false);
        emailField.setDisable(false);
        passwordField.setDisable(false);
        createAccountButton.setDisable(false);

        createAccountLink.setUnderline(false);
        createAccountLink.setTextFill(Color.BLACK);
    }

    public void signIn(ActionEvent actionEvent) {
    }

    public void createAccount(ActionEvent actionEvent) {
    }
}
