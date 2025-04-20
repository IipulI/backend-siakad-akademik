package com.siakad.service;

import com.siakad.entity.User;
import com.siakad.enums.MessageKey;
import jakarta.servlet.http.HttpServletRequest;

public interface UserActivityService {
    String getClientIpAddress(HttpServletRequest servletRequest);
    void saveUserActivity(HttpServletRequest servletRequest, MessageKey activity);
    User getCurrentUser();
}
