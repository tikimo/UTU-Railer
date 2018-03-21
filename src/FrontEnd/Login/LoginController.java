package FrontEnd.Login;

import BusinessLogic.DatabaseManager;
import FrontEnd.Portal.PortalController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    private static String authenticatedUser;
    private static DatabaseManager dbmanager = new DatabaseManager("users");

    public Label createAccountLink;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField emailField;
    public PasswordField passwordField;
    public TextField emailFieldLogin;
    public PasswordField passwordFieldLogin;
    public Button signInButton;
    public Button createAccountButton;
    public Label errorLabelSignIn;
    public Label errorLabelCreateAccount;

    public void showAccountCreationDialog() {
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
                System.err.println("Sign-in succeeded!");
                launchPortal(email, false);
            } else {
                System.err.println("Authentication failed!");
                errorLabelSignIn.setText("Authentication failed!");
            }

        } else {
            System.err.println("User does not exist!");
            errorLabelSignIn.setText("User does not exist!");
        }
    }

    public void createAccount() {

        if (dbmanager.userExists(emailField.getText())) {
            System.err.println("[WARNING] Email is already associated with another account! Aborting registration...");
            errorLabelCreateAccount.setTextFill(Color.RED);
            errorLabelCreateAccount.setText("Email already exists!");
        } else {
            // Crypting is handled in back-end
            dbmanager.addNewUser(firstNameField.getText(), lastNameField.getText(), emailField.getText(), passwordField.getText());
            System.err.println("New user added. You can login now.");
            errorLabelCreateAccount.setTextFill(Color.GREEN);
            errorLabelCreateAccount.setText("Account created! You can login now.");
        }
    }

    /**
     * Turns the textfield to encrypted password before sending it to database.
     * Assumes database only has encrypted passwords stored. Plaintext
     * never leaves clients PC.
     *
     *
     * @param email email address (key value)
     * @param plaintextPass Password as plaintext. Crypting in backend
     * @return returns true if auth succeeded
     * @throws NoSuchAlgorithmException Throws dbmanagers exception
     */
    private boolean authenticationSucceeded(String email, String plaintextPass)  {
        if (dbmanager.authenticate(email, plaintextPass)) { // rs.next() returns true if row exists
            System.err.println("User "+email+" successfully authenticated.");
            authenticatedUser = email;
            return true;
        } else {
            return false;
        }
    }

    public void launchPortal(String email, boolean debug) {
        try {
            // Setup new stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Portal/portal.fxml"));
            Parent portalRoot = fxmlLoader.load();
            Stage portalStage = new Stage();
            portalStage.setTitle("U-R Portal: " + email);
            portalStage.setScene(new Scene(portalRoot));

            // Set variables and parameters
            portalStage.setResizable(false);
            portalStage.setHeight(768);
            portalStage.setWidth(1024);
            portalStage.getIcons().add(new Image("FrontEnd/RES/logo.png"));


            if (!debug) {
            // Hide login and launch portal
            Stage loginStage = (Stage) signInButton.getScene().getWindow();
            loginStage.hide();
            }

            portalStage.show();

        } catch (IOException e) {
            System.err.println("[ERROR] Cant open new window!");
            e.printStackTrace();
        }



    }


    public static String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(String email) {
        this.authenticatedUser = email;
    }

    public static DatabaseManager getDbmanager() {
        return dbmanager;
    }
}
