package com.siakad.service.impl;

import com.siakad.dto.request.PenilaianKelasKuliahReqDto;
import com.siakad.dto.response.MahasiswaDto;
import com.siakad.dto.response.PenilaianKelasResDto;
import com.siakad.dto.transform.PenilaianTransform;
import com.siakad.dto.transform.helper.RincianKrsMappeHelper;
import com.siakad.entity.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.KrsKey;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.PenilaianKelasService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PenilaianKelasServiceImpl implements PenilaianKelasService {

    private final MahasiswaRepository mahasiswaRepository;
    private final KelasKuliahRepository kelasKuliahRepository;
    private final KomposisiPenilaianRepository komposisiPenilaianRepository;
    private final KomposisiNilaiMataKuliahRepository komposisiNilaiMataKuliahRepository;
    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final UserActivityService service;
    private final SkalaPenilaianRepository skalaPenilaianRepository;
    private final PenilaianTransform mapper;
    private final RincianKrsMappeHelper helper;

    @Transactional
    @Override
    public void updateNilaiKelas(UUID kelasId, PenilaianKelasKuliahReqDto dto, HttpServletRequest servletRequest) {

        var mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(dto.getMahasiswaId())
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

        KrsMahasiswa krsMahasiswa = krsMahasiswaRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(mahasiswa.getId())
                .orElseThrow(() -> new RuntimeException("KRS Mahasiswa tidak ditemukan"));

        KrsRincianMahasiswa krsRincianMahasiswa =
                krsRincianMahasiswaRepository.findFirstByKrsMahasiswaIdAndKelasIdAndPeriodeStatusActive(
                                krsMahasiswa.getId(), kelasId)
                        .orElseThrow(() -> new RuntimeException("KRS Rincian Mahasiswa tidak ditemukan"));

        BigDecimal nilai = hitungBobot(dto);
        int nilaiFinal = nilai.setScale(0, RoundingMode.HALF_UP).intValue();

        int sks = krsRincianMahasiswa.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka()
                + krsRincianMahasiswa.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum();

        SkalaPenilaian skala = skalaPenilaianRepository.findAll().stream()
                .filter(s -> !s.getIsDeleted()
                        && s.getNilaiMin().compareTo(BigDecimal.valueOf(nilaiFinal)) <= 0
                        && s.getNilaiMax().compareTo(BigDecimal.valueOf(nilaiFinal)) >= 0)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Skala penilaian tidak ditemukan untuk nilai: " + nilaiFinal));

        // Set nilai
        setDataNilai(krsRincianMahasiswa, dto, nilai, nilaiFinal, skala, sks);

        // Simpan
        krsRincianMahasiswaRepository.save(krsRincianMahasiswa);

        // Aktivitas pengguna
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KRS);
    }

    @Override
    public PenilaianKelasResDto getAllPenilaianKelas(UUID kelasId) {
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas tidak ditemukan"));

        List<Mahasiswa> mahasiswaList = krsRincianMahasiswaRepository.findMahasiswaByKelasKuliahId(kelasId);

        List<MahasiswaDto> mahasiswaDtoList = mapper.mahasiswaListToDtoList(mahasiswaList);

        for (int i = 0; i < mahasiswaList.size(); i++) {
            Mahasiswa mahasiswa = mahasiswaList.get(i);
            MahasiswaDto dto = mahasiswaDtoList.get(i);
            helper.mapRincianMahasiswa(dto, kelasId, mahasiswa.getId());
        }

        UUID mataKuliahId = kelasKuliah.getSiakMataKuliah().getId();

        PenilaianKelasResDto resDto = mapper.toDto(kelasKuliah);
        resDto.setMahasiswa(mahasiswaDtoList);
        List<KomposisiNilaiMataKuliah> komposisiList = komposisiNilaiMataKuliahRepository.findByMataKuliahId(mataKuliahId);

        List<KomposisiPenilaian> komponenList = komposisiList.stream()
                .map(KomposisiNilaiMataKuliah::getSiakKomposisiNilai)
                .toList();

        resDto.setKomposisiPenilaian(mapper.komponenPenilaianListToDtoList(komponenList));

        return resDto;
    }


    private void setDataNilai(KrsRincianMahasiswa rincian, PenilaianKelasKuliahReqDto dto,
                              BigDecimal nilai, int nilaiFinal, SkalaPenilaian skala, int sks) {

        rincian.setKehadiran(dto.getKehadiran());
        rincian.setUas(dto.getUas());
        rincian.setUts(dto.getUts());
        rincian.setTugas(dto.getTugas());
        rincian.setNilai(nilai);
        rincian.setStatus(getStatus(nilaiFinal));
        rincian.setHurufMutu(skala.getHurufMutu());
        rincian.setAngkaMutu(skala.getAngkaMutu());
        rincian.setNilaiAkhir(skala.getAngkaMutu().multiply(BigDecimal.valueOf(sks)));
        rincian.setUpdatedAt(LocalDateTime.now());
    }

    private String getStatus(int nilaiFinal) {
        return nilaiFinal >= 50 ? KrsKey.LULUS.getLabel() : KrsKey.GAGAL.getLabel();
    }

    private BigDecimal hitungBobot(PenilaianKelasKuliahReqDto dto) {
        Map<String, BigDecimal> nilaiMap = Map.of(
                "TUGAS", dto.getTugas(),
                "UTS", dto.getUts(),
                "UAS", dto.getUas(),
                "KEHADIRAN", dto.getKehadiran()
        );

        List<KomposisiPenilaian> komposisiList = komposisiPenilaianRepository.findAllByIsDeletedFalse();

        if (komposisiList.isEmpty()) {
            throw new RuntimeException("Komposisi penilaian tidak ditemukan.");
        }

        BigDecimal nilaiAkhir = BigDecimal.ZERO;

        for (KomposisiPenilaian komponen : komposisiList) {
            String namaKomponen = komponen.getNama().toUpperCase().trim();
            BigDecimal bobot = komponen.getPersentase().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal nilai = nilaiMap.getOrDefault(namaKomponen, BigDecimal.ZERO);
            nilaiAkhir = nilaiAkhir.add(nilai.multiply(bobot));
        }
        return nilaiAkhir.setScale(2, RoundingMode.HALF_UP);
    }
}
