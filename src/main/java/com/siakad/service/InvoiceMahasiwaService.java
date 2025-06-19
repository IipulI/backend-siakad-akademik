package com.siakad.service;

import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.request.TandaiLunasReqDto;
import com.siakad.dto.request.TanggalTenggatReqDto;
import com.siakad.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface InvoiceMahasiwaService {

    @Transactional
    List<InvoiceMahasiswaResDto> create(InvoiceMahasiswaReqDto dto, HttpServletRequest servletRequest);

    Page<MahasiswaKeuanganResDto> getPaginateMahasiswa(String keyword, String fakultas, String periodeMasuk, String sistemKuliah, String angkatan, Integer semester, String programStudi, String npm, Pageable pageable);

    Page<TagihanMahasiswaResDto> getPaginateTagihanMahasiswa(String keyword, String npm, String nama, Integer semester, String angkatan, String programStudi, String fakultas, String periodeAkademik, Pageable pageable);

    RingkasanTagihanResDto getRingkasanTagihan();

    void updateTanggalTenggatTagihan(UUID id, TanggalTenggatReqDto reqDto, HttpServletRequest servletRequest);

    void deleteInvoiceMahasiswa(UUID id, HttpServletRequest servletRequest);

    DetailRiwayatTagihanDto getOne(UUID id);

    void tandaiLunas(TandaiLunasReqDto reqDto, HttpServletRequest servletRequest);

    List<TransaksiLunasDto> getAllTagihanLunas();

    List<StatistikTagihanFakultasDto> getAllStatikTagihanFakultas();
}
