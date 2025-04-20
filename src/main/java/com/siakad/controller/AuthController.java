package com.siakad.controller;

import com.siakad.dto.UserInfo;
import com.siakad.dto.request.AuthReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.ApiResDto;
import com.siakad.dto.response.AuthResDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.service.AuthService;
import com.siakad.service.JwtService;
import com.siakad.service.MahasiswaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @Operation(summary = "Login User")
    @PostMapping("/login")
    public ResponseEntity<ApiResDto<AuthResDto>> login(
            @Valid @RequestBody AuthReqDto authRequest) {
        UserInfo userInfo = authService.authenticate(authRequest);
        String token = jwtService.generateToken(userInfo);

        return ResponseEntity.ok(
                ApiResDto.<AuthResDto>builder()
                        .status(MessageKey.SUCCESS.getMessage())
                        .message(MessageKey.LOGIN_SUCCESS.getMessage())
                        .data(AuthResDto.from(userInfo, token))
                        .build()
        );
    }
}
