package dailyNews.dailyNews.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    // Generate token
    public String generateToken(UserDetails userDetails) {
       String username = userDetails.getUsername();
       Date issuedAt = new Date();
       Date expiration = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

       SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

       return Jwts.builder()
               .setSubject(username)
               .setIssuedAt(issuedAt)
               .setExpiration(expiration)
               .signWith(key, SignatureAlgorithm.HS256)
               .compact();
    }

    // Extract User to check JWT
    public String extractUsername(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

           return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Check if token is valid
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Check if token has not expired
    private boolean isTokenExpired(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
