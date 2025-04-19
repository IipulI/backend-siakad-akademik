package com.siakad.service;

import com.siakad.dto.UserInfo;
import com.siakad.dto.request.AuthReqDto;

public interface AuthService {

    UserInfo authenticate(AuthReqDto authRequest);
}
