package com.siakad.service;

import com.siakad.dto.request.BatasSksReqDto;
import com.siakad.dto.response.BatasSksResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface BatasSksService {
    BatasSksResDto save(BatasSksReqDto dto, HttpServletRequest request);
    List<BatasSksResDto> getAll();
    BatasSksResDto getOne(UUID id);
    BatasSksResDto update (UUID id, BatasSksReqDto dto, HttpServletRequest request);
    void delete (UUID id, HttpServletRequest request);
}
