package FrontEnd.Login;

import BusinessLogic.DatabaseManager;
import FrontEnd.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
                System.err.println("Authentication succeeded!");
                launchPortal(email);
            } else {
                System.err.println("Authentication failed!");
            }

        } else {
            System.err.println("User does not exist!");
        }
    }

    public void createAccount() {

        if (dbmanager.userExists(emailField.getText())) {
            System.err.println("[WARNING] Email is already associated with another account! Aborting registration...");
        } else {
            // Crypting is handled in back-end
            dbmanager.addNewUser(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());
            System.err.println("New user added. You can login now.");
        }
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
        try {
            // Setup new stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Portal/portal.fxml"));
            Parent portalRoot = fxmlLoader.load();
            Stage portalStage = new Stage();
            portalStage.setTitle("Welcome to U-R Portal " + name);
            portalStage.setScene(new Scene(portalRoot));

            // Set variables and parameters
            portalStage.setResizable(false);
            portalStage.setHeight(768);
            portalStage.setWidth(1024);
            portalStage.getIcons().add(new Image("FrontEnd/RES/logo.png"));


            // Hide login and launch portal
            Stage loginStage = (Stage) signInButton.getScene().getWindow();
            loginStage.hide();
            portalStage.show();

        } catch (IOException e) {
            System.err.println("[ERROR] Cant open new window!");
        }



    }


}
