package com.siakad.service;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PembimbingAkademikService {
    List<PembimbingAkademikResDto> save(PembimbingAkademikReqDto reqDto, HttpServletRequest servletRequest);
    List<PembimbingAkademikResDto> getAll();
    Page<PembimbingAkademikResDto> getAllByPaginate(String periodeAkademik, String programStudi, Integer semester, String angkatan, Pageable pageable);
}
