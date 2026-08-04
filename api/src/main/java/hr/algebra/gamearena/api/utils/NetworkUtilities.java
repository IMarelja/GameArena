package hr.algebra.gamearena.api.utils;

import jakarta.servlet.http.HttpServletRequest;

public class NetworkUtilities {

    private static final String FORWARDED_FOR_HEADER = "X-Forwarded-For";

    private NetworkUtilities() {}

    public static String resolveClientAddress(HttpServletRequest request) {
        var forwardedFor = request.getHeader(FORWARDED_FOR_HEADER);

        if (forwardedFor != null && !forwardedFor.isBlank())
            return forwardedFor.split(",")[0].trim();

        return request.getRemoteAddr();
    }

    public static boolean isIpv6(String address) {
        return address != null && address.contains(":");
    }
}
