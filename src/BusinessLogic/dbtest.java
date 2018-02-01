package BusinessLogic;

import javafx.application.Application;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class dbtest extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager dbm = new DatabaseManager("users");

        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA-256");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digester != null;
        digester.update("vesipullo".getBytes());
        String salis = new String(digester.digest());
        System.out.println(salis);




        dbm.addNewUser("Vesi", "Pullo", "vesipullo@utu.fi", salis);
        System.out.println(dbm.getUserName("vesipullo@utu.fi"));

        System.out.println(dbm.userExists("vesipullo@utu.fi"));


        System.out.println(dbm.authenticate("vesipullo@utu.fi", salis));
        System.exit(0);
    }
}
