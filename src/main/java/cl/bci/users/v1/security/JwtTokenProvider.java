package cl.bci.users.v1.security;

import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cl.bci.users.v1.model.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    private static final String HMAC_SHA_256 = "HmacSHA256";

    @Value("${custom.jwt.secret}")
    private String secretBase64;

    @Value("${custom.jwt.expiration-ms:86400000}")
    private long expirationMs;

    @Value("${custom.jwt.issuer:new-create-api}")
    private String issuer;

    /**
     * Genera un JWT HS256.
     */
    public String generateToken(User user) {
        String subject = user.getId() != null ? user.getId().toString() : user.getEmail();
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + this.expirationMs);

        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setIssuer(this.issuer)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .signWith(signingKey(), SignatureAlgorithm.HS256);

        String token = builder.compact();
        LOG.debug("JWT generado para subject {} (expiracion {} ms)", subject,
                this.expirationMs);
        return token;
    }

    private SecretKey signingKey() {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(this.secretBase64);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(
                    "custom.jwt.secret no es un Base64 valido", ex);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
