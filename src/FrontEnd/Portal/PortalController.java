package FrontEnd.Portal;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

public class PortalController {

    public ScrollPane scrollPane;
    public AnchorPane scrollPaneAnchorPane;


    public void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVisible(false);
        
    }

}
