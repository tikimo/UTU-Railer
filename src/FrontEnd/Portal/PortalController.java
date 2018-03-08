package FrontEnd.Portal;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;

/**
 * 1. First the user must select the route
 *      FROM - TO
 *      Time of leaving / Arrival time
 *      Number of passengers
 *  Program will search the database for matching trains and return them. There must be enough seats!
 *  Seat configuration and position dont matter when travelling together.
 *
 *  2. Second is the position selection
 *  Seat will have seatType. Types:
 *      Allergy, Pet, disabled, quiet, family
 *  Whatever the type, user must be able to select the position to sit in.
 *
 *  3. Payment of commute
 *  User can pay in the application or in the train.
 *  After payment, program will declare seats taken.
 *  Upon seats declared taken, other clients will be notified.
 *
 *
 *  X. User settings tab will be activable at any time
 *  In this tab, user can delcare:
 *      Address information
 *      new password
 *      billing information
 *
 */
public class PortalController {

    public Button settingsButton;
    public AnchorPane settingsAnchorPane;
    private boolean settingsOpen = false;

    public void initialize() {
        Image buttonBgImage = new Image("FrontEnd/RES/hamburger.png");
        settingsButton.setGraphic(new ImageView(buttonBgImage));
        settingsAnchorPane.setVisible(settingsOpen);
    }


    public void toggleSettings(ActionEvent actionEvent) {
        settingsAnchorPane.setVisible(!settingsOpen);
        settingsOpen = !settingsOpen;
    }
}
