package com.siakad.service;

import com.siakad.dto.request.InvoiceKomponenMahasiswaReqDto;
import com.siakad.dto.response.InvoiceKomponenResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InvoiceKomponenService {
    InvoiceKomponenResDto create(InvoiceKomponenMahasiswaReqDto dto, HttpServletRequest servletRequest);
    Page<InvoiceKomponenResDto> getPaginated(String keyword, int page, int size);
    InvoiceKomponenResDto update(UUID id, InvoiceKomponenMahasiswaReqDto dto, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
