package pixels.pro.fit.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import pixels.pro.fit.dto.ApiException;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class TokenProvider {
    private final SecretKey secret;
    @Getter
    protected Integer expirationMinutes;

    public TokenProvider(String secretPayload, Integer expirationMinutes){
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretPayload));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(@NonNull String payload){
        final LocalDateTime now = LocalDateTime.now();
        final Instant expirationInstant = now.plusMinutes(expirationMinutes).atZone(ZoneId.systemDefault()).toInstant();
        final Date expiration = Date.from(expirationInstant);

        return Jwts.builder()
                .subject(payload)
                .expiration(expiration)
                .signWith(this.secret)
//                .claim("roles", user.getRoles())
//                .claim("firstName", user.getFirstName())
                .compact();

    }

    public boolean verifyToken(@NonNull String token) {
        Jwts.parser()
                .verifyWith(this.secret)
                .build()
                .parseSignedClaims(token);
        return true;
    }

    public Claims getClaims(@NonNull String token){
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
