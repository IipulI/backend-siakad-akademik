package com.siakad.service;

import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.RpsMataKuliahDto;
import com.siakad.dto.response.RpsDetailResDto;
import com.siakad.dto.response.RpsResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RpsService {

    void create(RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest request) throws IOException;
    RpsResDto getOne(UUID id);

    List<RpsMataKuliahDto> getRpsByMataKuliah(UUID mataKuliahId);

    RpsResDto update(UUID id, RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest servletRequest) throws IOException;
    byte[] getDokumenRps(UUID id);
    Page<RpsResDto> getPaginate(String tahunKurikulum, String programStudi, String periodeAkademik, Boolean hasKelas, Pageable pageable);
    void delete(UUID id, HttpServletRequest servletRequest);
    void createKelas(KelasRpsReqDto reqDto, HttpServletRequest request);
    RpsResDto getRpsByKelas(UUID kelasId);

    RpsDetailResDto getOneRpsDetail(UUID id, String whichId);
}
