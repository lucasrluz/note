package com.api.note.services.auth;

import java.util.Date;

import org.springframework.stereotype.Service;
import com.api.note.services.auth.util.JwtKeys;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {
    private JwtKeys jwtKeys;

    public JwtService(JwtKeys jwtKeys) {
        this.jwtKeys = jwtKeys;
    }

    public String generateJwt(String email) {
        return Jwts
            .builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + this.jwtKeys.getJwtExpirationMs()))
            .signWith(this.jwtKeys.getSecretKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    
    public boolean validateJwt(String jwt, String email) {
        Jwts.parserBuilder()
            .setSigningKey(this.jwtKeys.getSecretKey())
            .requireSubject(email)
            .build()
            .parseClaimsJws(jwt);
        
        return true;
    }
}
