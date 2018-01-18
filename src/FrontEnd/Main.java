package FrontEnd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage loginStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        loginStage.setTitle("Hello World");
        loginStage.setScene(new Scene(root, 300, 275));
        loginStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
