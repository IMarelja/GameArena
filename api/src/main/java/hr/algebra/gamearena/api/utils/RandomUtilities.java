package hr.algebra.gamearena.api.utils;

import java.security.SecureRandom;
import java.util.HexFormat;

public class RandomUtilities {

    private static final SecureRandom secureRandom = new SecureRandom();

    private RandomUtilities() {}

    public static String randomHex(int byteLength) {
        byte[] bytes = new byte[byteLength];
        secureRandom.nextBytes(bytes);
        return HexFormat.of().formatHex(bytes);
    }
}
