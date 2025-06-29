package com.siakad.service;

import com.siakad.dto.request.KurikulumProdiReqDto;
import com.siakad.dto.request.MataKuliahReqDto;
import com.siakad.dto.response.KurikulumProdiResDto;
import com.siakad.dto.response.MataKuliahCplCpmkResDto;
import com.siakad.dto.response.MataKuliahResDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface MataKuliahService {
    MataKuliahResDto create(MataKuliahReqDto request, HttpServletRequest servletRequest);
    Page<MataKuliahResDto> search(String keyword, String programStudi, String jenisMataKuliah, String tahunKurikulum, Pageable pageable);
    MataKuliahResDto getOne(UUID id);
    MataKuliahResDto update(MataKuliahReqDto request, UUID id, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
    void updateKurikulum(UUID id, KurikulumProdiReqDto request, HttpServletRequest servletRequest);
    List<KurikulumProdiResDto> getKurikulumPerSemester(String programStudi, String tahunKurikulum);
    MataKuliahCplCpmkResDto getMataKuliahCplCpmk(UUID id);

    // Dosen get mata kuliah
    Page<MataKuliahResDto> getPaginated(String keyword, UUID dosenId, Pageable pageable);
}
