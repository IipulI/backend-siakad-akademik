package com.siakad.service.impl;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.dto.transform.MahasiswaTransform;
import com.siakad.entity.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.enums.RoleType;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.MahasiswaService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MahasiswaServiceImpl implements MahasiswaService {

    private final MahasiswaTransform mapper;
    private final MahasiswaRepository mahasiswaRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserActivityService service;

    @Override
    @Transactional
    public MahasiswaResDto create(MahasiswaReqDto request, HttpServletRequest servletRequest) {
        if (mahasiswaRepository.existsByNpm(request.getNpm())) {
            throw new ApplicationException(ExceptionType.NPM_ALREADY_EXISTS, "NPM sudah digunakan");
        }

        if (mahasiswaRepository.existsByEmail(request.getEmail())) {
            throw new ApplicationException(ExceptionType.EMAIL_ALREADY_EXISTS, "Email sudah digunakan");
        }

        String password = request.getTanggalLahir().toString().replace("-", "");
        User user = createUserWithRole(request.getNpm(), request.getEmail(), password, RoleType.MAHASISWA);

        Mahasiswa mahasiswa = mapper.toEntity(request);
        mahasiswa.setEmail(user.getEmail());
        mahasiswa.setSiakUser(user);
        mahasiswa.setIsDeleted(false);
        mahasiswaRepository.save(mahasiswa);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_MAHASISWA);

        return mapper.toDto(mahasiswa);
    }


    @Override
    public Page<MahasiswaResDto> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<Mahasiswa> all = mahasiswaRepository.findAllNotDeleted(pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public MahasiswaResDto getOne(UUID id) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND, ExceptionType.USER_NOT_FOUND.getFormattedMessage("With id: " + id)));
        return mapper.toDto(mahasiswa);
    }


    @Override
    @Transactional
    public MahasiswaResDto update(UUID id, MahasiswaReqDto request, HttpServletRequest servletRequest) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "mahasiswa tidak valid dengan id: " + id));

        if (!mahasiswa.getNpm().equals(request.getNpm()) &&
                mahasiswaRepository.existsByNpm(request.getNpm())) {
            throw new ApplicationException(ExceptionType.NPM_ALREADY_EXISTS, "NPM sudah digunakan");
        }

        if (!mahasiswa.getEmail().equals(request.getEmail()) &&
                mahasiswaRepository.existsByEmail(request.getEmail())) {
            throw new ApplicationException(ExceptionType.EMAIL_ALREADY_EXISTS, "Email sudah digunakan");
        }

        if (!mahasiswa.getNpm().equals(request.getNpm()) && !mahasiswa.getEmail().equals(request.getEmail())) {
            String password = request.getTanggalLahir().toString().replace("-", "");
            User newUser = createUserWithRole(request.getNpm(), request.getEmail(), password, RoleType.MAHASISWA);
            mahasiswa.setSiakUser(newUser);
        }

        mapper.toEntity(request, mahasiswa);
        mahasiswa.setUpdatedAt(LocalDateTime.now());
        mahasiswa.setEmail(request.getEmail());
        mahasiswaRepository.save(mahasiswa);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_MAHASISWA);

        return mapper.toDto(mahasiswa);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "mahasiswa tidak valid dengan id: " + id
                ));

        mahasiswa.setIsDeleted(true);
        Mahasiswa save = mahasiswaRepository.save(mahasiswa);

        // Tambah Log
        service.saveUserActivity(servletRequest, MessageKey.DELETE_MAHASISWA);
        mapper.toDto(save);
    }

    @Override
    public User createUserWithRole(String username,
                                   String email,
                                   String password,
                                   RoleType roleType) {
        Role role = roleRepository.findByNama(roleType)
                .orElseThrow(() -> new ApplicationException(ExceptionType.ROLE_NOT_FOUND, "Role tidak valid"));

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        userRepository.save(user);

        UserRole userRole = UserRole.builder()
                .id(new UserRole.UserRoleId(user.getId(), role.getId()))
                .build();
        userRoleRepository.save(userRole);
        return user;
    }

}
