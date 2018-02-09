package BusinessLogic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypter {
    private MessageDigest digester;

    public String generatePasswordHash(String passwd) {
        String hashedPass;
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert digester != null;
        digester.update(passwd.getBytes());
        String cryptedpass = new String(digester.digest());
        System.out.println(cryptedpass);

        return cryptedpass;
    }
}
