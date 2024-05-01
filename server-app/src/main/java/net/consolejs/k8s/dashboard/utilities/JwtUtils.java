package net.consolejs.k8s.dashboard.utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.bson.types.ObjectId;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class JwtUtils {
    public static final MacAlgorithm ALG = Jwts.SIG.HS512;
    private static final String BEARER = "Bearer";
    private static SecretKey mySecretKey;

    public static String getTokenFromAuthHeader(String header) {
        if (header == null || header.isEmpty()) {
            return null;
        }
        String[] parts = header.split(" ");
        if (parts.length < 2 || !BEARER.equals(parts[0])) {
            return null;
        }

        return parts[1];
    }

    public static ObjectId getIdIfValid(String signedClaims, String jwtSecret) {
        if (signedClaims == null) {
            return null;
        }

        try {
            Claims claims = getClaims(signedClaims, jwtSecret);
            if (claims != null && claims.getExpiration()
                    .after(Date.from(Instant.now()))) {
                return new ObjectId(claims.getId());
            }
        } catch (ExpiredJwtException exception) {
            // Empty
        }
        return null;
    }

    public static String getToken(ObjectId objectId, Date expire, String jwtSecret) {
        return Jwts.builder()
                .id(objectId.toString())
                .signWith(JwtUtils.getSecretKey(jwtSecret), JwtUtils.ALG)
                .expiration(expire)
                .compact();
    }

    private static Claims getClaims(String signedClaims, String jwtSecret) {
        return Jwts.parser()
                .verifyWith(JwtUtils.getSecretKey(jwtSecret))
                .build()
                .parseSignedClaims(signedClaims)
                .getPayload();
    }

    private static synchronized SecretKey getSecretKey(String jwtSecret) {
        try {
            if (mySecretKey == null) {
                mySecretKey = getInternalSecretKey(jwtSecret);
            }
            return mySecretKey;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static SecretKey getInternalSecretKey(String jwtSecret) throws IOException {
        byte[] decoded = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(decoded);
    }
}
