package com.siakad.service;

import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.InvoiceMahasiswaResDto;
import com.siakad.dto.response.MahasiswaKeuanganResDto;
import com.siakad.dto.response.RingkasanTagihanResDto;
import com.siakad.dto.response.TagihanMahasiswaResDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceMahasiwaService {

    @Transactional
    List<InvoiceMahasiswaResDto> create(InvoiceMahasiswaReqDto dto, HttpServletRequest servletRequest);

    Page<MahasiswaKeuanganResDto> getPaginateMahasiswa(int page, int size);

    Page<TagihanMahasiswaResDto> getPaginateTagihanMahasiswa(int page, int size);

    RingkasanTagihanResDto getRingkasanTagihan();


}
