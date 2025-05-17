package com.siakad.service;

import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.RpsResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface RpsService {

    RpsResDto create(RpsReqDto reqDto, HttpServletRequest request);
    RpsResDto getOne(UUID id);
    RpsResDto update(UUID id)
}
