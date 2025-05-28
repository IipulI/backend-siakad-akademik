package com.siakad.service;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.response.KrsResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KrsService {

    void save(KrsReqDto dto, HttpServletRequest servletRequest);
    void update(KrsReqDto dto, HttpServletRequest servletRequest);
    Page<KrsResDto> getPaginated(String kelas, Pageable pageable);
    void updateStatus(HttpServletRequest servletRequest);
}
