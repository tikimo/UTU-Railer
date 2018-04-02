package FrontEnd.Login;

import BusinessLogic.DatabaseManager;
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

/**
 * This class is a controller for log-in window. In this window a user can
 * log in to an existing account or create a new one. This also detects
 * an admin login.
 */
public class LoginController {
    // Keep track of suthenticated user. This can then be retrieved from other controllers
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

    /**
     * Clickable text "Create account" runs this method. It set disable(false)
     * on all objects that were there.
     */
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
     * Sign in logic (Button)
     */
    public void signIn() {
        String email = emailFieldLogin.getText();
        String password = passwordFieldLogin.getText();

        if (dbmanager.userExists(email)) {  // check if user exists
            if (authenticationSucceeded(email, password)) { // check if users' name was returned
                // Here authentication has succeeded. Login window will close and portal will launch.
                if (dbmanager.isAdmin(email)) {
                    launchAdminDialog();
                } else {
                System.err.println("Sign-in succeeded!");
                launchPortal(email, false);
                }
            } else {
                System.err.println("Authentication failed!");
                errorLabelSignIn.setText("Authentication failed!");
            }

        } else {
            System.err.println("User does not exist!");
            errorLabelSignIn.setText("User does not exist!");
        }
    }

    /**
     * launches a new window (portal or admin prompt) with stage and hides the login window.
     */
    private void launchAdminDialog() {
        // Setup new stage
        try {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Login/adminLogin/adminLogin.fxml"));
        Parent adminLoginRoot = fxmlLoader.load();
        Stage adminLoginStage = new Stage();
        adminLoginStage.setTitle("Choose your portal");
        adminLoginStage.setScene(new Scene(adminLoginRoot));

        // Set variables and parameters
        adminLoginStage.setResizable(false);
        adminLoginStage.getIcons().add(new Image("FrontEnd/RES/logo.png"));

        //  Hide login stage and launch portal selector
        Stage loginStage = (Stage) signInButton.getScene().getWindow();
        loginStage.hide();
        adminLoginStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Creates a new account with given information. Starts complaining if
     * some of the required fields were empty.
     */
    public void createAccount() {
        if (firstNameField.getText().equals("") ||
                lastNameField.getText().equals("") ||
                emailField.getText().equals("") ||
                passwordField.getText().equals("")) {
            System.err.println("[ERROR] All fields were not filled.");
            errorLabelCreateAccount.setTextFill(Color.RED);
            errorLabelCreateAccount.setText("Please fill all the fields!");
        } else {
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
    }

    /**
     * Sends a plaintext password to backend. Crypting is done in backend.
     *
     * @param email email address (key value)
     * @param plaintextPass Password as plaintext. Crypting in backend
     * @return returns true if auth succeeded
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

    /**
     * This badboy launches the actual portal. If admin login is detected,
     * launch admin prompt first
     * @param email email field to launch portal with
     * @param debug debug mode for testing purposes
     */
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

    /**
     * Getter for user email that succeeded login
     * @return user email
     */
    public static String getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Used for debug purposes to force the authenticated user
     * @param email user to be set as authenticated
     */
    public void setAuthenticatedUser(String email) {
        authenticatedUser = email;
    }

    /**
     * Returns the databasemanager so it does not need to be initialized every time.
     * @return
     */
    public static DatabaseManager getDbmanager() {
        return dbmanager;
    }
}
