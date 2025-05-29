package com.siakad.service;

import com.siakad.dto.request.CapaianMataKuliahReqDto;
import com.siakad.dto.response.CapaianMataKuliahResDto;
import com.siakad.dto.response.MataKuliahCpmkMappingDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CapaianMataKuliahService {

    CapaianMataKuliahResDto save(UUID idMataKuliah, CapaianMataKuliahReqDto dto, HttpServletRequest servletRequest);

    CapaianMataKuliahResDto getOne(UUID idMataKuliah, UUID id);

    Page<CapaianMataKuliahResDto> getPaginate(UUID idMataKuliah, Pageable pageable);

    CapaianMataKuliahResDto update(UUID idMataKuliah, UUID id, CapaianMataKuliahReqDto dto, HttpServletRequest servletRequest);

    void delete(UUID idMataKuliah, UUID id, HttpServletRequest servletRequest);

    List<MataKuliahCpmkMappingDto> getMataKuliahWithCpmkStatus(String tahunKurikulum);
}
