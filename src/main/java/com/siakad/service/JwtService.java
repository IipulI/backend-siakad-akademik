package com.siakad.service;

import com.siakad.dto.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface JwtService {
    String generateToken(UserInfo userInfo);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    List<GrantedAuthority> getRolesFromToken(String token);
}
