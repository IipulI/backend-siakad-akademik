package com.siakad.service.impl;

import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.dto.transform.InvoiceTransform;
import com.siakad.dto.transform.TagihanMahasiswaTransform;
import com.siakad.entity.InvoiceKomponen;
import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.InvoiceKomponenRepository;
import com.siakad.repository.InvoiceMahasiswaRepository;
import com.siakad.repository.InvoicePembayaranKomponenMahasiswaRepository;
import com.siakad.repository.MahasiswaRepository;
import com.siakad.service.InvoiceMahasiwaService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceMahasiwaServiceImpl implements InvoiceMahasiwaService {

    private final InvoiceMahasiswaRepository invoiceMahasiswaRepository;
    private final InvoicePembayaranKomponenMahasiswaRepository InvoicePembayaranKomponenMahasiswaRepository;
    private final InvoiceTransform mapper;
    private final TagihanMahasiswaTransform mapperTagihanMahasiswa;
    private final UserActivityService service;
    private final InvoiceKomponenRepository invoiceKomponenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final InvoicePembayaranKomponenMahasiswaRepository invoicePembayaranKomponenMahasiswaRepository;

    @Transactional
    @Override
    public InvoiceMahasiswaResDto create(InvoiceMahasiswaReqDto dto,
                                         HttpServletRequest servletRequest) {

        var invoice = mapper.toEntity(dto);
        invoice.setIsDeleted(false);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());

        List<InvoicePembayaranKomponenMahasiswa> pembayaranList = new ArrayList<>();
        BigDecimal totalTagihan = BigDecimal.ZERO;

        for (InvoiceKomponenReqDto komponenDto : dto.getKomponen()) {
            InvoiceKomponen komponen = invoiceKomponenRepository.findById(komponenDto.getKomponenId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komponen tidak ditemukan :" + komponenDto.getKomponenId()));

            InvoicePembayaranKomponenMahasiswa pembayaran = new InvoicePembayaranKomponenMahasiswa();
            pembayaran.setInvoiceMahasiswa(invoice);
            pembayaran.setInvoiceKomponen(komponen);
            pembayaran.setTagihan(komponenDto.getTagihan());
            pembayaran.setIsDeleted(false);
            pembayaran.setCreatedAt(LocalDateTime.now());
            pembayaran.setUpdatedAt(LocalDateTime.now());

            pembayaranList.add(pembayaran);
            totalTagihan = totalTagihan.add(komponenDto.getTagihan());
        }

        invoice.setTotalTagihan(totalTagihan);
        invoice.setTotalBayar(BigDecimal.ZERO);

        invoice.setInvoicePembayaranKomponenMahasiswaList(pembayaranList);

        invoice = invoiceMahasiswaRepository.save(invoice);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_INVOICE_MAHASISWA);

        return mapper.toResDto(invoice);
    }

    @Override
    public Page<MahasiswaKeuanganResDto> getPaginateMahasiswa(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<Mahasiswa> all = mahasiswaRepository.findByWithRelasiNative(pageable);
        return all.map(mapper::toKeuanganDto);

    }

    @Override
    public Page<TagihanMahasiswaResDto> getPaginateTagihanMahasiswa(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<InvoicePembayaranKomponenMahasiswa> all =
                invoicePembayaranKomponenMahasiswaRepository.findByIsDeletedFalse(pageable);
        return all.map(mapperTagihanMahasiswa::toDto);
    }

    @Override
    public RingkasanTagihanResDto getRingkasanTagihan() {
        List<InvoicePembayaranKomponenMahasiswa> data = invoicePembayaranKomponenMahasiswaRepository.findAllByIsDeletedFalse();

        BigDecimal totalTagihan = data.stream()
                .map(e -> e.getInvoiceKomponen().getNominal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTerbayar = data.stream()
                .map(InvoicePembayaranKomponenMahasiswa::getTagihan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RingkasanTagihanSourceDto source = new RingkasanTagihanSourceDto();
        source.setTotalTagihan(totalTagihan);
        source.setTotalTerbayar(totalTerbayar);

        return mapperTagihanMahasiswa.toDto(source);
    }
}
