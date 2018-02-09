package FrontEnd.Login;

import BusinessLogic.DatabaseManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

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

        if (dbmanager.userExists(email)) {  // check if user exists
            if (authenticationSucceeded(email, password)) { // check if users' name was returned
                // Here authentication has succeeded. Login window will close and portal will launch.
                System.err.println("Authentication succeeded");
                launchPortal(email);
            }

        } else {
            System.err.println("User does not exist!");
        }
    }

    public void createAccount() {
        // Crypting is handled in back-end
        dbmanager.addNewUser(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());
        System.err.println("Users password is: "+passwordField.getText());
        System.err.println("New user added. You can login now.");
    }

    /**
     * Turns the textfield to encrypted password before sending it to database.
     * Assumes database only has encrypted passwords stored. Plaintext
     * never leaves clients PC.
     *
     *
     * @param email
     * @param plaintextPass
     * @return
     * @throws NoSuchAlgorithmException
     */
    private boolean authenticationSucceeded(String email, String plaintextPass)  {
        return dbmanager.authenticate(email, plaintextPass); // rs.next() returns true if row exists
    }

    private void launchPortal(String email) {
        String name = dbmanager.getUserName(email);

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Portal/portal.fxml"));
        } catch (IOException e) { e.printStackTrace(); }

        Stage Portal = new Stage();
        Portal.setTitle("UTU-Railer Portal: " + name);
        Portal.setScene(new Scene(root, 1200, 800));
        Portal.setResizable(false);
        Portal.show();

    }
}
