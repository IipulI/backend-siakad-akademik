package com.siakad.service.impl;

import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.*;
import com.siakad.dto.transform.InvoiceTransform;
import com.siakad.dto.transform.TagihanMahasiswaTransform;
import com.siakad.entity.InvoiceKomponen;
import com.siakad.entity.InvoiceMahasiswa;
import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.InvoiceKey;
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
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceMahasiwaServiceImpl implements InvoiceMahasiwaService {

    private final InvoiceMahasiswaRepository invoiceMahasiswaRepository;
    private final InvoiceTransform mapper;
    private final TagihanMahasiswaTransform mapperTagihanMahasiswa;
    private final UserActivityService service;
    private final InvoiceKomponenRepository invoiceKomponenRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final InvoicePembayaranKomponenMahasiswaRepository invoicePembayaranKomponenMahasiswaRepository;

    @Transactional
    @Override
    public List<InvoiceMahasiswaResDto> create(InvoiceMahasiswaReqDto dto, HttpServletRequest servletRequest) {
        List<InvoiceMahasiswaResDto> results = new ArrayList<>();

        List<KomponenInfo> komponenInfoList = validateAndGetKomponenInfo(dto.getKomponen());
        BigDecimal totalTagihanPerInvoice = calculateTotalTagihan(komponenInfoList);

        for (UUID mahasiswaId : dto.getSiakMahasiswaIds()) {
            Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Mahasiswa tidak ditemukan: " + mahasiswaId));

            var invoice = createInvoiceForMahasiswa(dto, mahasiswa, komponenInfoList, totalTagihanPerInvoice);
            results.add(mapper.toResDto(invoice));
        }

        service.saveUserActivity(servletRequest, MessageKey.CREATE_INVOICE_MAHASISWA);
        return results;
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
                .map(e -> e.getInvoiceMahasiswa().getTotalBayar())
                .reduce(BigDecimal.ZERO, BigDecimal::add);



        RingkasanTagihanSourceDto source = new RingkasanTagihanSourceDto();
        source.setTotalTagihan(totalTagihan);
        source.setTotalTerbayar(totalTerbayar);

        return mapperTagihanMahasiswa.toDto(source);
    }

    private static class KomponenInfo {
        InvoiceKomponen komponen;
        BigDecimal nominal;

        KomponenInfo(InvoiceKomponen komponen, BigDecimal nominal) {
            this.komponen = komponen;
            this.nominal = nominal;
        }
    }

    private List<KomponenInfo> validateAndGetKomponenInfo(List<InvoiceKomponenReqDto> komponenDtos) {
        List<KomponenInfo> komponenInfoList = new ArrayList<>();
        for (InvoiceKomponenReqDto komponenDto : komponenDtos) {
            InvoiceKomponen komponen = invoiceKomponenRepository.findById(komponenDto.getKomponenId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Komponen tidak ditemukan: " + komponenDto.getKomponenId()));

            if (komponen.getNominal() == null) {
                throw new ApplicationException(ExceptionType.BAD_REQUEST,
                        "Nominal komponen tidak boleh kosong untuk komponen ID: " + komponen.getId());
            }

            komponenInfoList.add(new KomponenInfo(komponen, komponen.getNominal()));
        }
        return komponenInfoList;
    }

    private BigDecimal calculateTotalTagihan(List<KomponenInfo> komponenInfoList) {
        return komponenInfoList.stream()
                .map(info -> info.nominal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private InvoiceMahasiswa createInvoiceForMahasiswa(InvoiceMahasiswaReqDto dto, Mahasiswa mahasiswa,
                                                       List<KomponenInfo> komponenInfoList, BigDecimal totalTagihan) {

        var invoice = mapper.toEntity(dto);
        invoice.setSiakMahasiswa(mahasiswa);
        invoice.setIsDeleted(false);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setTotalTagihan(totalTagihan);
        invoice.setTotalBayar(BigDecimal.ZERO);
        invoice.setKodeInvoice("kode");
        invoice.setStatus(InvoiceKey.BELUM_LUNAS.getLabel());

        List<InvoicePembayaranKomponenMahasiswa> pembayaranList = createPembayaranList(komponenInfoList, invoice);
        invoice.setInvoicePembayaranKomponenMahasiswaList(pembayaranList);

        return invoiceMahasiswaRepository.save(invoice);
    }

    private List<InvoicePembayaranKomponenMahasiswa> createPembayaranList(
            List<KomponenInfo> komponenInfoList,
            InvoiceMahasiswa invoice) {

        List<InvoicePembayaranKomponenMahasiswa> pembayaranList = new ArrayList<>();
        for (KomponenInfo info : komponenInfoList) {
            InvoicePembayaranKomponenMahasiswa pembayaran = new InvoicePembayaranKomponenMahasiswa();
            pembayaran.setInvoiceMahasiswa(invoice);
            pembayaran.setInvoiceKomponen(info.komponen);
            pembayaran.setTagihan(info.nominal);
            pembayaran.setIsDeleted(false);
            pembayaran.setCreatedAt(LocalDateTime.now());
            pembayaran.setUpdatedAt(LocalDateTime.now());

            pembayaranList.add(pembayaran);
        }
        return pembayaranList;
    }
}
