package com.siakad.service.impl;

import com.siakad.dto.UserInfo;
import com.siakad.entity.Role;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.RoleRepository;
import com.siakad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository siakUserRepository;
    private final RoleRepository siakRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User siakUser = siakUserRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND, "User not found with: " + usernameOrEmail
                ));
        List<Role> siakRoles = siakRoleRepository.findByUserId(siakUser.getId());

        return UserInfo.builder()
                .user(siakUser)
                .roles(siakRoles)
                .build();

    }
}
