package com.siakad.service;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.entity.User;
import com.siakad.enums.RoleType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface MahasiswaService {
    MahasiswaResDto create(MahasiswaReqDto request, HttpServletRequest servletRequest);
    Page<MahasiswaResDto> getPaginated(int page, int size);
    MahasiswaResDto getOne(UUID id);
    MahasiswaResDto update(UUID id, MahasiswaReqDto request, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
    User createUserWithRole(String username, String email, String password, RoleType roleType);
}
