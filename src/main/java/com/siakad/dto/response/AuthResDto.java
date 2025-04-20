package com.siakad.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.siakad.dto.UserInfo;
import com.siakad.entity.Role;
import com.siakad.enums.RoleType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResDto {
    private String token;
    private LoginResDto user;

    public static AuthResDto from(UserInfo userInfo, String token) {
        return AuthResDto.builder()
                .token(token)
                .user(LoginResDto.from(userInfo))
                .build();
    }

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LoginResDto {
        private UUID id;
        private String username;
        private List<RoleType> roles;

        public static LoginResDto from(UserInfo userInfo){
            return LoginResDto.builder()
                    .id(userInfo.getUser().getId())
                    .username(userInfo.getUser().getUsername())
                    .roles(userInfo.getRoles().stream()
                            .map(Role::getNama).toList())
                    .build();
        }
    }
}
