package com.siakad.service;

import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.KelasRpsResponseDto;
import com.siakad.dto.response.RpsResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface RpsService {

    RpsResDto create(RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest request) throws IOException;
    RpsResDto getOne(UUID id);
    RpsResDto update(UUID id, RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest servletRequest) throws IOException;
    byte[] getDokumenRps(UUID id);

    Page<RpsResDto> getPaginate(String tahunKurikulum, String programStudi, String periodeAkademik, Boolean hasKelas, Pageable pageable);

    void delete(UUID id, HttpServletRequest servletRequest);
    KelasRpsResponseDto createKelas(KelasRpsReqDto reqDto, HttpServletRequest request);


}
