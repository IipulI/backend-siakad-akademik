package com.siakad.service.impl;

import com.siakad.entity.User;
import com.siakad.entity.UserActivity;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.UserActivityRepository;
import com.siakad.repository.UserRepository;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final UserRepository userRepository;

    @Override
    public String getClientIpAddress(HttpServletRequest servletRequest) {
        String xfHeader = servletRequest.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return servletRequest.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public void saveUserActivity(HttpServletRequest servletRequest, MessageKey activity) {
        User user = getCurrentUser();
        String ipAddress = getClientIpAddress(servletRequest);

        UserActivity userActivity = UserActivity.builder()
                .siakUser(user)
                .activity(activity.getMessage())
                .waktu(LocalDateTime.now())
                .ipAddress(ipAddress)
                .build();

        userActivityRepository.save(userActivity);
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND, "User tidak ditemukan"));
    }
}
