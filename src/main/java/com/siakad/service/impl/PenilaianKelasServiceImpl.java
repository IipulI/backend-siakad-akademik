package com.siakad.service.impl;

import com.siakad.dto.request.PenilaianKelasKuliahReqDto;
import com.siakad.entity.KomposisiPenilaian;
import com.siakad.entity.KrsMahasiswa;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.enums.KrsKey;
import com.siakad.enums.MessageKey;
import com.siakad.repository.KomposisiPenilaianRepository;
import com.siakad.repository.KrsMahasiswaRepository;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import com.siakad.repository.MahasiswaRepository;
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
    private final KomposisiPenilaianRepository komposisiPenilaianRepository;
    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final UserActivityService service;

    @Transactional
    @Override
    public void updateNilaiKelas(PenilaianKelasKuliahReqDto dto, HttpServletRequest servletRequest) {

        var mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(dto.getMahasiswaId())
                .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

        KrsMahasiswa krsMahasiswa = krsMahasiswaRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(dto.getMahasiswaId())
                .orElseThrow(() -> new RuntimeException("Krs Mahasiswa tidak ditemukan"));

        krsMahasiswa.setSiakMahasiswa(mahasiswa);
        krsMahasiswa.setUpdatedAt(LocalDateTime.now());

        // Bingung disini
        KrsRincianMahasiswa krsRincianMahasiswa =
                krsRincianMahasiswaRepository.findFirstByKrsMahasiswaIdAndKelasIdAndStatusNullOrEmpty(krsMahasiswa.getId(), dto.getKelasId())
                        .orElseThrow(() -> new RuntimeException("Krs Rincian Mahasiswa tidak ditemukan"));

        krsRincianMahasiswa.setKehadiran(dto.getKehadiran());
        krsRincianMahasiswa.setUas(dto.getUas());
        krsRincianMahasiswa.setUts(dto.getUts());
        krsRincianMahasiswa.setTugas(dto.getTugas());
        BigDecimal nilai = hitungBobot(dto);
        krsRincianMahasiswa.setNilai(nilai);
        int nilaiFinal = nilai.setScale(0, RoundingMode.HALF_UP).intValue();

        int sks = krsRincianMahasiswa.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka()
                + krsRincianMahasiswa.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum();

        BigDecimal nilaiAkhir = angkaMutu(nilaiFinal).multiply(BigDecimal.valueOf(sks));
        krsRincianMahasiswa.setStatus(getStatus(nilaiFinal));
        krsRincianMahasiswa.setHurufMutu(hurufMutu(nilaiFinal));
        krsRincianMahasiswa.setAngkaMutu(angkaMutu(nilaiFinal));
        krsRincianMahasiswa.setNilaiAkhir(nilaiAkhir);
        krsRincianMahasiswaRepository.save(krsRincianMahasiswa);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KRS);
    }


    private String getStatus(int nilaiFinal) {
        if (nilaiFinal >= 50) {
            return KrsKey.LULUS.getLabel();
        } else {
            return KrsKey.GAGAL.getLabel();
        }
    }

    private BigDecimal hitungBobot(PenilaianKelasKuliahReqDto dto) {
        Map<String, BigDecimal> nilaiMap = Map.of(
                "TUGAS", dto.getTugas(),
                "UTS", dto.getUts(),
                "UAS", dto.getUas(),
                "KEHADIRAN", dto.getKehadiran()
        );

        BigDecimal nilaiAkhir = BigDecimal.ZERO;

        List<KomposisiPenilaian> komposisiList = komposisiPenilaianRepository.findAllByIsDeletedFalse();


        for (KomposisiPenilaian komponen : komposisiList) {
            String namaKomponen = komponen.getNama().toUpperCase();
            BigDecimal bobot = komponen.getPersentase().divide(BigDecimal.valueOf(100));

            BigDecimal nilai = nilaiMap.getOrDefault(namaKomponen, BigDecimal.ZERO);
            nilaiAkhir = nilaiAkhir.add(nilai.multiply(bobot));
        }

        return nilaiAkhir = nilaiAkhir.setScale(2, RoundingMode.HALF_UP);
    }


    private String hurufMutu(int nilaiFinal) {
        String grade;
        if (nilaiFinal >= 85) {
            return grade = "A";
        } else if (nilaiFinal >= 75) {
            return grade = "B";
        } else if (nilaiFinal >= 65) {
            return grade = "C";
        } else if (nilaiFinal >= 50) {
            return grade = "D";
        } else {
            return grade = "E";
        }
    }

    private BigDecimal angkaMutu(int nilaiFinal) {
        BigDecimal angkaMutu;
        if (nilaiFinal >= 85) {
           return angkaMutu = BigDecimal.valueOf(4.00);
        } else if (nilaiFinal >= 80) {
            return angkaMutu = BigDecimal.valueOf(3.70);
        } else if (nilaiFinal >= 75) {
            return angkaMutu = BigDecimal.valueOf(3.30);
        } else if (nilaiFinal >= 70) {
            return angkaMutu = BigDecimal.valueOf(3.00);
        } else if (nilaiFinal >= 65) {
            return angkaMutu = BigDecimal.valueOf(2.70);
        } else if (nilaiFinal >= 60) {
            return angkaMutu = BigDecimal.valueOf(2.30);
        } else if (nilaiFinal >= 55) {
            return angkaMutu = BigDecimal.valueOf(2.00);
        } else if (nilaiFinal >= 50) {
            return angkaMutu = BigDecimal.valueOf(1.00);
        } else {
            return angkaMutu = BigDecimal.ZERO;
        }
    }
}
