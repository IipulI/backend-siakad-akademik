package com.siakad.service;

import com.siakad.dto.UserInfo;
import com.siakad.dto.request.AuthRequest;

public interface AuthService {

    UserInfo authenticate(AuthRequest authRequest);
}
