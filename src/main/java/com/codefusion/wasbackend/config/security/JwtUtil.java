package com.codefusion.wasbackend.config.security;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.sql.Date;
import java.util.List;

@Component
@Data
public class JwtUtil {
    private SecretKey key;

    public JwtUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String username, List<String> roles, Long userId, Long storeId, Long companyId) {
        long currentTimeMillis = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .claim("userId", String.valueOf(userId))
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + 3600000))
                .signWith(key);

        if (storeId != null && (roles.contains("MANAGER") || roles.contains("EMPLOYEE"))) {
            jwtBuilder.claim("storeId", String.valueOf(storeId));
        }
        if (companyId != null && (roles.contains("BOSS") )) {
            jwtBuilder.claim("companyId", String.valueOf(companyId));
        }

        return jwtBuilder.compact();

    }
}
