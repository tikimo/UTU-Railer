package BusinessLogic;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A crypting engine for storing passwords securely, UTU-Railer would be useless without this :)
 * Uses a SHA-1 hashing method to store passwords. SHA-1 is SQLite friendly
 */
class Crypter {

    static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash.toString();
    }
}
