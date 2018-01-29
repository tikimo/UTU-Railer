package FrontEnd.Login;

import BusinessLogic.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    private DatabaseManager dbmanager = new DatabaseManager("users");

    public void showAccountCreationDialog(MouseEvent mouseEvent) {
        firstNameField.setDisable(false);
        lastNameField.setDisable(false);
        emailField.setDisable(false);
        passwordField.setDisable(false);
        createAccountButton.setDisable(false);

        createAccountLink.setUnderline(false);
        createAccountLink.setTextFill(Color.BLACK);
    }

    /**
     * Sign in logic.
     */
    public void signIn() {
        String email = emailFieldLogin.getText();
        String password = passwordFieldLogin.getText();

        if (dbmanager.userExists(email)) {
            if (authenticationSucceeded(email, password)) {

            }

        } else {
            System.err.println("User does not exist!");
        }
    }

    /**
     * Turns the textfield to encrypted password before sending it to database.
     * Assumes database only has encrypted passwords stored. Plaintext
     * never leaves clients PC.
     *
     *
     * @param email
     * @param PlaintextPass
     * @return
     * @throws NoSuchAlgorithmException
     */
    private boolean authenticationSucceeded(String email, String PlaintextPass)  {
        MessageDigest mdgst = null;
        try {
            mdgst = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert mdgst != null;
        mdgst.update(PlaintextPass.getBytes());
        String CryptPass = new String(mdgst.digest());

        return dbmanager.authenticate(email, CryptPass);
    }

    public void createAccount(ActionEvent actionEvent) {
    }
}
