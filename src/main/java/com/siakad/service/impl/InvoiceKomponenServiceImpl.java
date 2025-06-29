package com.siakad.service.impl;

import com.siakad.dto.request.InvoiceKomponenMahasiswaReqDto;
import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.response.InvoiceKomponenResDto;
import com.siakad.dto.transform.InvoiceTransform;
import com.siakad.entity.InvoiceKomponen;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.InvoiceKomponenRepository;
import com.siakad.service.InvoiceKomponenService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceKomponenServiceImpl implements InvoiceKomponenService {

    private final InvoiceKomponenRepository invoiceKomponenRepository;
    private final InvoiceTransform mapper;
    private final UserActivityService service;

    @Override
    public InvoiceKomponenResDto create(InvoiceKomponenMahasiswaReqDto dto, HttpServletRequest servletRequest) {
        InvoiceKomponen entity = mapper.toEntity(dto);
        entity.setIsDeleted(false);
        InvoiceKomponen saved = invoiceKomponenRepository.save(entity);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_INVOICE_MAHASISWA);
        return mapper.toDto(saved);
    }

    @Override
    public Page<InvoiceKomponenResDto> getPaginated(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);


        Page<InvoiceKomponen> all;
        if (keyword != null && !keyword.isEmpty()) {
            all = invoiceKomponenRepository.findAllNotDeleted(keyword, pageable);
        } else {
            all = invoiceKomponenRepository.findAllAndIsDeletedIsFalse(pageable);
        }

        return all.map(mapper::toDto);
    }

    @Override
    public InvoiceKomponenResDto update(UUID id, InvoiceKomponenMahasiswaReqDto dto, HttpServletRequest servletRequest) {
        var komponen = invoiceKomponenRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komponen tidak ditemukan : " + id));

        mapper.toEntity(dto, komponen);
        komponen.setUpdatedAt(LocalDateTime.now());
        komponen.setIsDeleted(false);
        invoiceKomponenRepository.save(komponen);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_INVOICE_KOMPONEN_MAHASISWA);

        return mapper.toDto(komponen);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        InvoiceKomponen komponen = invoiceKomponenRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komponen tidak ditemukan : " + id));

        komponen.setIsDeleted(true);
        var save = invoiceKomponenRepository.save(komponen);

        service.saveUserActivity(servletRequest, MessageKey.DELETE_INVOICE_KOMPONER_MAHASISWA);

        mapper.toDto(save);
    }
}
