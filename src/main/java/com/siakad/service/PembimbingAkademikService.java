package com.siakad.service;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PembimbingAkademikService {
    List<PembimbingAkademikResDto> save(PembimbingAkademikReqDto reqDto, HttpServletRequest servletRequest);
    List<PembimbingAkademikResDto> getAll();
}
