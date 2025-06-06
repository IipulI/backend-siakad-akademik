package com.siakad.service.impl;

import com.siakad.dto.UserInfo;
import com.siakad.dto.request.AuthReqDto;
import com.siakad.enums.ExceptionType;
import com.siakad.exception.ApplicationException;
import com.siakad.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public UserInfo authenticate(AuthReqDto authRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                            authRequest.getPassword())
            );
            return (UserInfo) authentication.getPrincipal();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ExceptionType.INVALID_PASSWORD);
        }
    }
}
