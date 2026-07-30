package hr.algebra.gamearena.api.utils;

import hr.algebra.gamearena.api.exceptions.extenders.CryptographicOperationException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class SecurityUtilities {

    private static final SecureRandom secureRandom = new SecureRandom();

    private SecurityUtilities() {}

    public static String hashPasswordWithSalt(String password, String salt) {
        try {
            // I really don't feel like using BCryptPasswordEncoder because I do not care I want to finish this
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt + password).getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptographicOperationException("No such algorithm exists or it is invalid: " + ex.getMessage());
        }
    }

    public static String saltForPassword() {
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        return HexFormat.of().formatHex(saltBytes);
    }
}

