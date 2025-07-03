package com.siakad.service.impl;

import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.request.TandaiLunasReqDto;
import com.siakad.dto.request.TanggalTenggatReqDto;
import com.siakad.dto.response.*;
import com.siakad.dto.transform.InvoiceTransform;
import com.siakad.dto.transform.TagihanMahasiswaTransform;
import com.siakad.entity.*;
import com.siakad.entity.service.InvoicePembayaranSpecification;
import com.siakad.entity.service.MahasiswaSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.InvoiceKey;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.InvoiceMahasiwaService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class InvoiceMahasiwaServiceImpl implements InvoiceMahasiwaService {

    private final InvoiceMahasiswaRepository invoiceMahasiswaRepository;
    private final FakultasRepository fakultasRepository;
    private final InvoiceTransform mapper;
    private final TagihanMahasiswaTransform mapperTagihanMahasiswa;
    private final UserActivityService service;
    private final InvoiceKomponenRepository invoiceKomponenRepository;
    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final InvoicePembayaranKomponenMahasiswaRepository invoicePembayaranKomponenMahasiswaRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;

    @Transactional
    @Override
    public List<InvoiceMahasiswaResDto> create(InvoiceMahasiswaReqDto dto, HttpServletRequest servletRequest) {
        List<InvoiceMahasiswaResDto> results = new ArrayList<>();

        for (UUID mahasiswaId : dto.getSiakMahasiswaIds()) {
            Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Mahasiswa tidak ditemukan: " + mahasiswaId));

            List<KomponenInfo> komponenInfoList = validateAndGetKomponenInfo(dto.getKomponen(), mahasiswa);
            BigDecimal totalTagihanPerInvoice = calculateTotalTagihan(komponenInfoList);

            var invoice = createInvoiceForMahasiswa(dto, mahasiswa, komponenInfoList, totalTagihanPerInvoice);
            results.add(mapper.toResDto(invoice));
        }
        service.saveUserActivity(servletRequest, MessageKey.CREATE_INVOICE_MAHASISWA);
        return results;
    }

    @Override
    public Page<MahasiswaKeuanganResDto> getPaginateMahasiswa(String keyword, String fakultas, String periodeMasuk, String sistemKuliah, String angkatan, Integer semester, String programStudi, Pageable pageable) {
        MahasiswaSpecification specBuilder = new MahasiswaSpecification();
        Specification<Mahasiswa> spec = specBuilder.entitySearch(keyword, fakultas, periodeMasuk, sistemKuliah, angkatan, semester, programStudi, null, null, null, null, null, null, null);

        Page<Mahasiswa> all = mahasiswaRepository.findAll(spec, pageable);
        return all.map(mapper::toKeuanganDto);
    }

    @Override
    public Page<TagihanMahasiswaResDto> getPaginateTagihanMahasiswa(String keyword, Integer semester, String angkatan, String programStudi, String fakultas, String periodeAkademik, Pageable pageable) {
        InvoicePembayaranSpecification specBuilder = new InvoicePembayaranSpecification();
        Specification<InvoicePembayaranKomponenMahasiswa> spec = specBuilder.entitySearch(keyword, semester, angkatan, programStudi, fakultas, periodeAkademik);

        Page<InvoicePembayaranKomponenMahasiswa> all = invoicePembayaranKomponenMahasiswaRepository.findAll(spec, pageable);
        return all.map(mapperTagihanMahasiswa::toDto);
    }

    @Override
    public RingkasanTagihanResDto getRingkasanTagihan() {
        List<InvoicePembayaranKomponenMahasiswa> data = invoicePembayaranKomponenMahasiswaRepository.findAllByIsDeletedFalse();

        BigDecimal totalTagihan = data.stream()
                .map(InvoicePembayaranKomponenMahasiswa::getTagihan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTerbayar = data.stream()
                .map(e -> e.getInvoiceMahasiswa().getTotalBayar())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RingkasanTagihanSourceDto source = new RingkasanTagihanSourceDto();
        source.setTotalTagihan(totalTagihan);
        source.setTotalTerbayar(totalTerbayar);

        return mapperTagihanMahasiswa.toDto(source);
    }

    @Override
    public void updateTanggalTenggatTagihan(UUID id, TanggalTenggatReqDto reqDto, HttpServletRequest servletRequest) {
        InvoiceMahasiswa invoiceMahasiswa = invoiceMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Invoice tidak ditemukan : " + id));

        invoiceMahasiswa.setId(id);
        invoiceMahasiswa.setTanggalTenggat(reqDto.getTanggalTenggat());
        invoiceMahasiswa.setUpdatedAt(LocalDateTime.now());
        invoiceMahasiswaRepository.save(invoiceMahasiswa);
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_INVOICE_MAHASISWA);
    }

    @Override
    public void deleteInvoiceMahasiswa(UUID id, HttpServletRequest servletRequest) {
        InvoiceMahasiswa invoiceMahasiswa = invoiceMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Invoice tidak ditemukan : " + id));
        invoiceMahasiswa.setIsDeleted(true);
        invoiceMahasiswaRepository.save(invoiceMahasiswa);

        InvoicePembayaranKomponenMahasiswa invoicePembayaranKomponenMahasiswa = invoicePembayaranKomponenMahasiswaRepository.findByInvoiceMahasiswa_IdAndIsDeletedFalse(invoiceMahasiswa.getId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tagihan tidak ditemukan : " + invoiceMahasiswa.getId()));
        invoicePembayaranKomponenMahasiswa.setIsDeleted(true);
        invoicePembayaranKomponenMahasiswaRepository.save(invoicePembayaranKomponenMahasiswa);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_INVOICE_MAHASISWA);
    }

    @Override
    public DetailRiwayatTagihanDto getOne(UUID id) {
        InvoiceMahasiswa invoiceMahasiswa = invoiceMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

        DetailRiwayatTagihanDto dto = new DetailRiwayatTagihanDto();
        dto.setKodeInvoice(invoiceMahasiswa.getKodeInvoice());
        dto.setPeriodeAkademik(invoiceMahasiswa.getSiakPeriodeAkademik().getNamaPeriode());
        dto.setMetodeBayar(invoiceMahasiswa.getMetodeBayar());
        dto.setTanggalBayar(invoiceMahasiswa.getTanggalBayar());
        dto.setTotalBayar(invoiceMahasiswa.getTotalBayar());
        dto.setNpm(invoiceMahasiswa.getSiakMahasiswa().getNpm());
        dto.setNama(invoiceMahasiswa.getSiakMahasiswa().getNama());

        ProgramStudiResDto programStudiResDto = new ProgramStudiResDto();
        programStudiResDto.setId(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getId());
        programStudiResDto
                .setNamaProgramStudi(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getNamaProgramStudi());

        JenjangResDto jenjangResDto = new JenjangResDto();
        jenjangResDto.setId(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getId());
        jenjangResDto.setNama(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getNama());
        jenjangResDto
                .setJenjang(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getJenjang());
        programStudiResDto.setJenjang(jenjangResDto);
        dto.setProgramStudiResDto(programStudiResDto);

        List<InvoiceMahasiswa> invoices = invoiceMahasiswaRepository
                .findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarNotNull(invoiceMahasiswa.getSiakMahasiswa().getId());

        List<TagihanKomponenDto> komponenList = invoices.stream()
                .flatMap(invoice -> invoice.getInvoicePembayaranKomponenMahasiswaList().stream())
                .map(komponen -> TagihanKomponenDto.builder()
                        .kodeKomponen(komponen.getInvoiceKomponen().getKodeKomponen())
                        .namaKomponen(komponen.getInvoiceKomponen().getNama())
                        .tagihan(komponen.getInvoiceKomponen().getNominal())
                        .build())
                .toList();
        dto.setTagihanKomponenDtos(komponenList);
        return dto;
    }

    @Override
    public void tandaiLunas(TandaiLunasReqDto reqDto, HttpServletRequest servletRequest) {

        for (UUID invoiceId : reqDto.getInvoiceMahasiswaIds()) {
            InvoiceMahasiswa invoiceMahasiswa = invoiceMahasiswaRepository
                    .findByIdAndIsDeletedFalse(invoiceId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Invoice tidak ditemukan: " + invoiceId));

            if (invoiceMahasiswa.getTanggalBayar() != null) {
                throw new ApplicationException(ExceptionType.BAD_REQUEST,
                        "Invoice sudah dibayar: " + invoiceId);
            }

            invoiceMahasiswa.setTanggalBayar(LocalDate.now());
            invoiceMahasiswa.setTotalBayar(invoiceMahasiswa.getTotalTagihan());
            invoiceMahasiswa.setMetodeBayar(InvoiceKey.MANUAL.getLabel());
            invoiceMahasiswa.setTahap(InvoiceKey.TAHAP2.getLabel());
            invoiceMahasiswa.setStatus(InvoiceKey.LUNAS.getLabel());

            invoiceMahasiswaRepository.save(invoiceMahasiswa);

            InvoicePembayaranKomponenMahasiswa komponen = invoicePembayaranKomponenMahasiswaRepository
                    .findByInvoiceMahasiswa_IdAndIsDeletedFalse(invoiceId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Tagihan komponen tidak ditemukan untuk invoice: " + invoiceId));

            komponen.setTagihan(BigDecimal.ZERO);
            invoicePembayaranKomponenMahasiswaRepository.save(komponen);
        }

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_INVOICE_MAHASISWA);
    }



    @Override
    public List<TransaksiLunasDto> getAllTagihanLunas() {
        List<InvoiceMahasiswa> invoiceMahasiswaList = invoiceMahasiswaRepository.findAllByTanggalBayarIsNotNullAndIsDeletedFalse();
        return invoiceMahasiswaList.stream()
                .map(invoice -> new TransaksiLunasDto(
                        invoice.getId(),
                        invoice.getSiakMahasiswa().getNama(),
                        invoice.getMetodeBayar(),
                        invoice.getTanggalBayar(),
                        invoice.getTotalBayar()
                ))
                .toList();
    }

    @Override
    public List<StatistikTagihanFakultasDto> getAllStatikTagihanFakultas() {
        BigDecimal totalTagihanSemua = invoiceMahasiswaRepository.sumTotalTagihanAktif();

        if (totalTagihanSemua.compareTo(BigDecimal.ZERO) == 0) {
            return Collections.emptyList();
        }

        // Dapatkan data per fakultas
        List<Fakultas> fakultasList = fakultasRepository.findAllByIsDeletedFalse();

        return fakultasList.stream()
                .map(fakultas -> {
                    BigDecimal totalTagihanFakultas = invoiceMahasiswaRepository
                            .sumTotalTagihanByFakultas(fakultas.getId());

                    double persentase = totalTagihanFakultas
                            .divide(totalTagihanSemua, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();

                    return new StatistikTagihanFakultasDto(
                            fakultas.getNamaFakultas(),
                            persentase
                    );
                })
                .sorted(Comparator.comparingDouble(StatistikTagihanFakultasDto::getPersentase).reversed())
                .toList();
    }
    private static class KomponenInfo {
        InvoiceKomponen komponen;
        BigDecimal nominal;

        KomponenInfo(InvoiceKomponen komponen, BigDecimal nominal) {
            this.komponen = komponen;
            this.nominal = nominal;
        }
    }

    private List<KomponenInfo> validateAndGetKomponenInfo(List<InvoiceKomponenReqDto> komponenDtos, Mahasiswa mahasiswa) {
        List<KomponenInfo> komponenInfoList = new ArrayList<>();
        for (InvoiceKomponenReqDto komponenDto : komponenDtos) {
            InvoiceKomponen komponen = invoiceKomponenRepository.findById(komponenDto.getKomponenId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Komponen tidak ditemukan: " + komponenDto.getKomponenId()));

            BigDecimal nominal;
            if("SKS".equalsIgnoreCase(komponen.getNama())){
                Integer totalSks = krsMahasiswaRepository.getJumlahSksDiambilByMahasiswaId(mahasiswa.getId());
                if (totalSks == null || komponen.getNominal() == null) {
                    throw new ApplicationException(ExceptionType.BAD_REQUEST,
                            "Total SKS tidak boleh kosong untuk komponen ID: " + komponen.getId());
                }

                nominal = komponen.getNominal().multiply(BigDecimal.valueOf(totalSks));
            } else {
                if (komponen.getNominal() == null) {
                    throw new ApplicationException(ExceptionType.BAD_REQUEST,
                            "Nominal komponen tidak boleh kosong untuk komponen ID: " + komponen.getId());
                }
                nominal = komponen.getNominal();
            }
            komponenInfoList.add(new KomponenInfo(komponen, nominal ));
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


        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik ACTIVE tidak ditemukan"));
        List<InvoiceMahasiswa> invoiceSebelumnya = invoiceMahasiswaRepository.findBySiakPeriodeAkademikAndSiakMahasiswa(periodeAkademik, mahasiswa);

        int nomorUrut = invoiceSebelumnya.size() + 1;
        String formattedUrut = String.format("%03d", nomorUrut);

        String generateKodeInvoice = "INV/" +
                periodeAkademik.getKodePeriode() + "/" +
                dto.getTanggalTenggat() + "-" +
                formattedUrut + "/" + mahasiswa.getNpm();

        invoice.setSiakMahasiswa(mahasiswa);
        invoice.setIsDeleted(false);
        invoice.setSiakPeriodeAkademik(periodeAkademik);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setTotalTagihan(totalTagihan);
        invoice.setTotalBayar(BigDecimal.ZERO);
        invoice.setKodeInvoice(generateKodeInvoice);
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
