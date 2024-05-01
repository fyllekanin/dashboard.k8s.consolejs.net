package net.consolejs.k8s.dashboard.utilities;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashUtility {

    public static String getHashedPassword(String password) {
        return BCrypt.withDefaults()
                .hashToString(12, password.toCharArray());
    }

    public static boolean isPasswordEqualHash(String password, String hash) {
        return BCrypt.verifyer()
                .verify(password.toCharArray(), hash)
                .verified;
    }
}
