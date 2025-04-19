package com.siakad.service.impl;

import com.siakad.config.JwtSecretConfig;
import com.siakad.dto.UserInfo;
import com.siakad.enums.ExceptionType;
import com.siakad.exception.ApplicationException;
import com.siakad.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signKey;

    @Override
    public String generateToken(UserInfo userInfo) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtSecretConfig.getJwtExpirationMs());

        // Extract role names from UserInfo
        List<String> roleNames = userInfo.getRoles().stream()
                .map(role -> "ROLE_" + role.getNama().name())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .claim("roles", roleNames)
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

            log.error("Invalid JWT: {}", ex.getMessage());
            throw new ApplicationException(ExceptionType.BAD_REQUEST, "Token tidak valid atau kadaluwarsa");
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

    @Override
    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        @SuppressWarnings("unchecked")
        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
