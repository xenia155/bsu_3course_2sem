package by.quantumquartet.quanthink.security;

import static by.quantumquartet.quanthink.services.AppLogger.logError;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import by.quantumquartet.quanthink.models.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        String tokenType = "Bearer ";
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(tokenType)) {
            return headerAuth.substring(tokenType.length());
        }
        return null;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logError(JwtUtils.class, "Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            logError(JwtUtils.class, "JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            logError(JwtUtils.class, "JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            logError(JwtUtils.class, "JWT claims string is empty: " + e.getMessage());
        }

        return false;
    }
}
