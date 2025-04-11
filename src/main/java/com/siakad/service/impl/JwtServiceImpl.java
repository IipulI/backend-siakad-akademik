package com.siakad.service.impl;

import com.siakad.config.JwtSecretConfig;
import com.siakad.dto.UserInfo;
import com.siakad.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signKey;

    @Override
    public String generateToken(UserInfo userInfo) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtSecretConfig.getJwtExpirationMs());
        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(signKey)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(signKey)
                    .build();
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (SignatureException | IllegalArgumentException | UnsupportedJwtException | MalformedJwtException |
                 ExpiredJwtException ex) {
            throw new IllegalArgumentException("sdfsdf");
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build();
        return jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject();

    }
}
