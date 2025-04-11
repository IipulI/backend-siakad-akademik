package com.siakad.service.impl;

import com.siakad.dto.UserInfo;
import com.siakad.entity.Role;
import com.siakad.entity.User;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error"));
        List<Role> roles = roleRepository.findByUserId(user.getId());

        return UserInfo.builder()
                .user(user)
                .roles(roles)
                .build();

    }
}
