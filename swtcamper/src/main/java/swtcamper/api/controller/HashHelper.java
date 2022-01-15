package swtcamper.api.controller;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class HashHelper {
    public static String hashIt(String inputToHash) {
        String hashedString = null;

        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputToHash.getBytes());

            byte[] bytes = md.digest();

            StringBuilder sb  = new StringBuilder();
            for(int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedString = sb.toString();
        } catch(NoSuchAlgorithmException e ) {
            System.out.println("No such algorithm");
        }

        return hashedString;
    }

}
