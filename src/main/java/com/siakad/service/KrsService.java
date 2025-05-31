package com.siakad.service;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.response.KrsResDto;
import com.siakad.dto.response.MengulangResDto;
import com.siakad.dto.response.PesertaKelas;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface KrsService {

    void save(KrsReqDto dto, HttpServletRequest servletRequest);
    void update(KrsReqDto dto, HttpServletRequest servletRequest);
    Page<KrsResDto> getPaginated(String kelas, Pageable pageable);
    void updateStatus(HttpServletRequest servletRequest);

    List<PesertaKelas> getPesertaKelas(UUID kelasId);
    List<MengulangResDto> getAllMengulang(UUID mahasiswaId);
}
