package com.siakad.service.impl;

import com.siakad.dto.response.*;
import com.siakad.dto.transform.helper.CardMapperHelper;
import com.siakad.dto.transform.helper.ProgramStudiMapperHelper;
import com.siakad.entity.*;
import com.siakad.repository.*;
import com.siakad.service.DashboardService;
import com.siakad.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final CardMapperHelper cardMapperHelper;
    private final MahasiswaRepository mahasiswaRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final UserActivityService service;
    private final InvoiceMahasiswaRepository invoiceMahasiswaRepository;
    private final InvoicePembayaranKomponenMahasiswaRepository invoicePembayaranKomponenMahasiswaRepository;
    private final HasilStudiRepository hasilStudiRepository;

    @Override
    public CardDto getCard() {
        CardDto cardDto = new CardDto();
        cardDto.setMahasiswaBaruPeriodeIni(cardMapperHelper.getMahasiswaBaruPeriodeIni());
        cardDto.setMahasiswaAktif(cardMapperHelper.getMahasiswaAktif());
        cardDto.setMahasiswaTerdaftar(cardMapperHelper.getMahasiswaTerdaftar());
        cardDto.setPeriodeSaatIni(cardMapperHelper.getPeriodeAkademikAktif());
        return cardDto;
    }

    @Override
    public List<AkmAngkatanDto> getAkmAngkatan() {
        List<String> angkatanList = mahasiswaRepository.findDistinctAngkatan();
        List<AkmAngkatanDto> result = new ArrayList<>();

        for (String angkatan : angkatanList) {
            int aktif = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "ACTIVE");
            int cuti = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "CUTI");
            int nonAktif = mahasiswaRepository.countByAngkatanAndStatus(angkatan, "NON_ACTIVE");

            AkmAngkatanDto dto = new AkmAngkatanDto();
            dto.setAngkatan(angkatan);
            dto.setAktif(aktif);
            dto.setCuti(cuti);
            dto.setNonAktif(nonAktif);
            dto.setTotal(aktif + cuti + nonAktif);

            result.add(dto);
        }

        return result;
    }

    @Override
    public List<AkmProdiDto> getJumlahMahasiswaPerProdi() {
        List<ProgramStudi> programStudis = programStudiRepository.findAllByIsDeletedFalse();

        List<AkmProdiDto> result = new ArrayList<>();

        for (ProgramStudi ps : programStudis) {
            int aktif = mahasiswaRepository.countAktifByProdi(ps.getId());
            int cuti = mahasiswaRepository.countCutiByProdi(ps.getId());
            int nonAktif = mahasiswaRepository.countNonAktifByProdi(ps.getId());
            int total = aktif + cuti + nonAktif;

            ProgramStudiResDto prodiDto = new ProgramStudiResDto();
            prodiDto.setId(ps.getId());
            prodiDto.setNamaProgramStudi(ps.getNamaProgramStudi());

            Jenjang jenjang = ps.getSiakJenjang();
            JenjangResDto jenjangDto = new JenjangResDto();
            jenjangDto.setId(jenjang.getId());
            jenjangDto.setNama(jenjang.getNama());
            jenjangDto.setJenjang(jenjang.getJenjang());
            prodiDto.setJenjang(jenjangDto);

            AkmProdiDto akmProdiDto = new AkmProdiDto();
            akmProdiDto.setProgramStudiResDto(prodiDto);
            akmProdiDto.setAktif(aktif);
            akmProdiDto.setCuti(cuti);
            akmProdiDto.setNonAktif(nonAktif);
            akmProdiDto.setTotal(total);

            result.add(akmProdiDto);
        }

        return result;
    }

    @Override
    public List<MahasiswaBaruDto> getMahasiswaBaru() {
        List<ProgramStudi> allProdi = programStudiRepository.findAllByIsDeletedFalse();
        List<MahasiswaBaruDto> result = new ArrayList<>();

        for (ProgramStudi ps : allProdi) {
            int total = mahasiswaRepository.countBySiakProgramStudiAndIsDeletedFalse(ps);
            int lakiLaki = mahasiswaRepository.countBySiakProgramStudiAndJenisKelaminAndIsDeletedFalse(ps, "Laki-laki");
            int perempuan = mahasiswaRepository.countBySiakProgramStudiAndJenisKelaminAndIsDeletedFalse(ps, "Perempuan");
            int pdb = mahasiswaRepository.countBySiakProgramStudiAndNoTerdaftarIsNotNullAndIsDeletedFalse(ps);
            int lainnya = mahasiswaRepository.countBySiakProgramStudiAndNoTerdaftarIsNullAndIsDeletedFalse(ps);

            JenjangResDto jenjang = new JenjangResDto();
            jenjang.setId(ps.getSiakJenjang().getId());
            jenjang.setNama(ps.getSiakJenjang().getNama());
            jenjang.setJenjang(ps.getSiakJenjang().getJenjang());

            ProgramStudiResDto prodiDto = new ProgramStudiResDto();
            prodiDto.setId(ps.getId());
            prodiDto.setNamaProgramStudi(ps.getNamaProgramStudi());
            prodiDto.setJenjang(jenjang);

            JenisKelaminDto jk = new JenisKelaminDto();
            jk.setLakiLaki(lakiLaki);
            jk.setPerempuan(perempuan);

            PendaftaranDto daftar = new PendaftaranDto();
            daftar.setPdb(pdb);
            daftar.setLainnya(lainnya);

            MahasiswaBaruDto dto = new MahasiswaBaruDto();
            dto.setProgramStudiResDto(prodiDto);
            dto.setJenisKelaminDto(jk);
            dto.setPendaftaranDto(daftar);
            dto.setTotal(total);

            result.add(dto);
        }

        return result;
    }

    @Override
    public TagihanMahasiswaDto getTagihanMahasiswa() {
        User currentUser = service.getCurrentUser();

        InvoiceMahasiswa invoiceMahasiswa = invoiceMahasiswaRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(currentUser.getSiakMahasiswa().getId())
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

        InvoicePembayaranKomponenMahasiswa invoicePembayaranKomponenMahasiswa = invoicePembayaranKomponenMahasiswaRepository.findByInvoiceMahasiswa_IdAndIsDeletedFalse(invoiceMahasiswa.getId())
                .orElseThrow(() -> new RuntimeException("Invoice Mahasiswa tidak ditemukan"));


        TagihanMahasiswaDto dto = new TagihanMahasiswaDto();
        dto.setTotalTagihan(invoiceMahasiswa.getTotalTagihan());
        dto.setTenggatTagihan(invoiceMahasiswa.getTanggalTenggat());
        dto.setTotalLunas(invoiceMahasiswa.getTotalBayar());
        dto.setTagihan(invoicePembayaranKomponenMahasiswa.getTagihan());

        return dto;
    }

    @Override
    public GrafikAkademikDto getGrafikAkademik() {
        User currentUser = service.getCurrentUser();
        UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

        // 1. Ambil IPS list urut semester
        List<BigDecimal> ipsList = hasilStudiRepository.findIpsByMahasiswa(mahasiswaId);

        // 2. Ambil IPK dari hasil studi semester terakhir
        HasilStudi latest = hasilStudiRepository.findLatestByMahasiswa(mahasiswaId)
                .orElseThrow(() -> new RuntimeException("Data hasil studi tidak ditemukan"));
        BigDecimal ipk = latest.getIpk();

        // 3. Hitung SKS kumulatif (yang lulus)
        Integer sksKumulatif = hasilStudiRepository.sumSksLulusByMahasiswa(mahasiswaId);
        if (sksKumulatif == null) sksKumulatif = 0;

        // 4. Estimasi jumlah mata kuliah: sks dibagi 3 atau 2 (rata-rata 3 sks per matkul)
        int rataSksPerMatkul = 3;
        int mataKuliahKumulatif = sksKumulatif / rataSksPerMatkul;

        // 5. Buat DTO
        GrafikAkademikDto dto = new GrafikAkademikDto();
        dto.setIps(ipsList);
        dto.setIpk(ipk);
        dto.setSksKumulatif(sksKumulatif);
        dto.setMataKuliahKumulatif(mataKuliahKumulatif);

        return dto;
    }

    @Override
    public TagihanKomponenMahasiswaDto getTagihanKomponenMahasiswa() {
        User currentUser = service.getCurrentUser();
        UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

        List<InvoiceMahasiswa> invoices = invoiceMahasiswaRepository
                .findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarIsNull(mahasiswaId);

        if (invoices.isEmpty()) {
            throw new RuntimeException("Invoice tidak ditemukan");
        }

        List<TagihanKomponenDto> komponenList = invoices.stream()
                .flatMap(invoice -> invoice.getInvoicePembayaranKomponenMahasiswaList().stream())
                .map(komponen -> TagihanKomponenDto.builder()
                        .kodeKomponen(komponen.getInvoiceKomponen().getKodeKomponen())
                        .namaKomponen(komponen.getInvoiceKomponen().getNama())
                        .tagihan(komponen.getInvoiceKomponen().getNominal())
                        .build())
                .toList();

        BigDecimal totalTagihan = komponenList.stream()
                .map(TagihanKomponenDto::getTagihan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TagihanKomponenMahasiswaDto.builder()
                .tagihanKomponen(komponenList)
                .totalTagihan(totalTagihan)
                .build();
    }

    @Override
    public List<RiwayatTagihanDto> getRiwayatTagihan() {
        User currentUser = service.getCurrentUser();
        UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

        List<InvoiceMahasiswa> invoices = invoiceMahasiswaRepository
                .findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarNotNull(mahasiswaId);

        List<RiwayatTagihanDto> riwayatTagihanDtos = new ArrayList<>();
        for (InvoiceMahasiswa invoice : invoices) {
            RiwayatTagihanDto dto = new RiwayatTagihanDto();
            dto.setId(invoice.getId());
            dto.setKodeInvoice(invoice.getKodeInvoice());
            dto.setMetodeBayar(invoice.getMetodeBayar());
            dto.setPeriodeAkademik(invoice.getSiakPeriodeAkademik().getNamaPeriode());
            dto.setTotalPembayaran(invoice.getTotalBayar());
            dto.setTanggalBayar(invoice.getTanggalBayar());

            riwayatTagihanDtos.add(dto);
        }

        return riwayatTagihanDtos;
    }

    @Override
    public DetailRiwayatTagihanDto getDetailRiwayatTagihan(UUID id) {

        User currentUser = service.getCurrentUser();
        UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

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
        programStudiResDto.setNamaProgramStudi(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getNamaProgramStudi());

        JenjangResDto jenjangResDto = new JenjangResDto();
        jenjangResDto.setId(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getId());
        jenjangResDto.setNama(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getNama());
        jenjangResDto.setJenjang(invoiceMahasiswa.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang().getJenjang());
        programStudiResDto.setJenjang(jenjangResDto);
        dto.setProgramStudiResDto(programStudiResDto);

        List<InvoiceMahasiswa> invoices = invoiceMahasiswaRepository
                .findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarNotNull(mahasiswaId);

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

}
