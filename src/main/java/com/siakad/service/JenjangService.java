package com.siakad.service;

import com.siakad.dto.request.JenjangReqDto;
import com.siakad.dto.response.JenjangResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface JenjangService {
    JenjangResDto save(JenjangReqDto dto, HttpServletRequest servletRequest);
    List<JenjangResDto> getAll();
    JenjangResDto getOne(UUID id);
    JenjangResDto update(UUID id, JenjangReqDto dto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
