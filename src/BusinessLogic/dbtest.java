package BusinessLogic;

import javafx.application.Application;
import javafx.stage.Stage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class dbtest extends Application {
    String fname = "Tijam", lname = "Moradi", email = "tikimo@utu.fi", pass = "huippusalainen";


    @Override
    public void start(Stage primaryStage) {
        // Crypt password
        Crypter crypter = new Crypter();
        String cpass = crypter.generateHash(pass);
        System.out.println("pass after crypt (MD5): "+cpass);

        // create user with the params
        DatabaseManager dbm = new DatabaseManager("users");
        dbm.addNewUser(fname,lname,email,pass);

        System.out.println(
        dbm.userExists("tikimo@utu.fi") + "\n" +
        dbm.getUserName("tikimo@utu.fi") + "\n" +
        dbm.authenticate("tikimo@utu.fi", pass) + "\n" +
        dbm.authenticate("tikimo@utu.fi", reverseLetters(pass)));

        System.exit(0);

        /*
        Crypter crypter = new Crypter();
        System.out.println(crypter.generatePasswordHash(salis));
        dbm.addNewUser("Vesi", "Pullo", "vesipullo@utu.fi", salis);
        System.out.println(dbm.getUserName("vesipullo@utu.fi"));

        System.out.println(dbm.userExists("vesipullo@utu.fi"));


        System.out.println(dbm.authenticate("vesipullo@utu.fi", salis));
        System.exit(0);
        */
    }

    private String reverseLetters(String pass) {
        char last = pass.charAt(pass.length()-1);
        char secondLast = pass.charAt(pass.length()-2);
        String done = pass.substring(0,pass.length()-2) + last + secondLast;
        return done;
    }
}
