package com.siakad.controller;

import com.siakad.dto.UserInfo;
import com.siakad.dto.request.AuthRequest;
import com.siakad.dto.response.ApiResponse;
import com.siakad.dto.response.AuthResponse;
import com.siakad.service.AuthService;
import com.siakad.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest){
        UserInfo userInfo = authService.authenticate(authRequest);
        String token = jwtService.generateToken(userInfo);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .status("success")
                        .message("Login successful")
                        .data(AuthResponse.from(userInfo, token))
                        .build()
        );
    }
}
