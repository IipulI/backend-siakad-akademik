package com.siakad.service.impl;

import com.siakad.dto.response.KomponenKomposisiNilaiMataKuliahMhsDto;
import com.siakad.dto.response.KomposisiNilaiMataKuliahMhsResDto;
import com.siakad.entity.*;
import com.siakad.repository.KomposisiNilaiMataKuliahRepository;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import com.siakad.service.KomposisiNilaiMataKuliahMhsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KomposisiNilaiMataKuliahMhsServiceImpl implements KomposisiNilaiMataKuliahMhsService {

    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final KomposisiNilaiMataKuliahRepository komposisiNilaiRepository;


    @Override
    public List<KomposisiNilaiMataKuliahMhsResDto> getKomposisiMataKuliah(UUID mahasiswaId, UUID periodeAkademikId){
        List<KrsRincianMahasiswa> enrollments = krsRincianMahasiswaRepository
                .findAllByMahasiswaAndPeriodeWithDetails(mahasiswaId, periodeAkademikId);

        if (enrollments.isEmpty()) {
            return Collections.emptyList();
        }

        List<UUID> mataKuliahIds = enrollments.stream()
                .map(e -> e.getSiakKelasKuliah().getSiakMataKuliah().getId())
                .distinct()
                .collect(Collectors.toList());

        Map<UUID, List<KomposisiNilaiMataKuliah>> componentsByMataKuliahId = komposisiNilaiRepository
                .findAllByMataKuliahIn(mataKuliahIds)
                .stream()
                .collect(Collectors.groupingBy(c -> c.getSiakMataKuliah().getId()));

        return enrollments.stream().map(enrollment -> {
            KelasKuliah kelas = enrollment.getSiakKelasKuliah();
            MataKuliah mataKuliah = kelas.getSiakMataKuliah();
            List<KomposisiNilaiMataKuliah> components = componentsByMataKuliahId
                    .getOrDefault(mataKuliah.getId(), Collections.emptyList());

            List<KomponenKomposisiNilaiMataKuliahMhsDto> komponenDtoList = components.stream().map(comp -> {
                KomposisiPenilaian penilaian = comp.getSiakKomposisiNilai();
                return KomponenKomposisiNilaiMataKuliahMhsDto.builder()
                        .namaKomposisi(penilaian.getNama())
                        .persentase(penilaian.getPersentase())
                        .nilai(getScoreFromEnrollment(enrollment, penilaian.getKey()))
                        .build();
            }).collect(Collectors.toList());

            return KomposisiNilaiMataKuliahMhsResDto.builder()
                    .tahunKurikulum(mataKuliah.getSiakTahunKurikulum().getTahun())
                    .kodeMataKuliah(mataKuliah.getKodeMataKuliah())
                    .namaMataKuliah(mataKuliah.getNamaMataKuliah())
                    .namaKelas(kelas.getNama())
                    .komposisiNilaiMataKuliahResDto(komponenDtoList)
                    .nilaiAkhir(enrollment.getNilaiAkhir())
                    .build();
        }).collect(Collectors.toList());
    }

    private BigDecimal getScoreFromEnrollment(KrsRincianMahasiswa enrollment, String key) {
        if (!StringUtils.hasText(key)) {
            return BigDecimal.ZERO;
        }

        switch (key.toLowerCase()) {
            case "uts":
                return enrollment.getUts() != null ? enrollment.getUts() : BigDecimal.ZERO;
            case "uas":
                return enrollment.getUas() != null ? enrollment.getUas() : BigDecimal.ZERO;
            case "tugas":
                return enrollment.getTugas() != null ? enrollment.getTugas() : BigDecimal.ZERO;
            case "kehadiran":
                return enrollment.getKehadiran() != null ? enrollment.getKehadiran() : BigDecimal.ZERO;
            default:
                // If key doesn't match a known column, return 0
                return BigDecimal.ZERO;
        }
    }
}
