package com.siakad.service.impl;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PesertaKelasReqDto;
import com.siakad.dto.response.KrsResDto;
import com.siakad.dto.response.MengulangResDto;
import com.siakad.dto.response.PeriodeResDto;
import com.siakad.dto.request.PindahKelasReqDto;
import com.siakad.dto.response.KrsMenungguResDto;
import com.siakad.dto.response.PesertaKelas;
import com.siakad.dto.transform.KrsTransform;
import com.siakad.dto.transform.PesertaKelasTransform;
import com.siakad.entity.*;
import com.siakad.entity.service.KrsSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.KrsKey;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.KrsService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KrsServiceImpl implements KrsService {

    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final KelasKuliahRepository kelasKuliahRepository;
    private final KrsTransform mapper;
    private final UserActivityService service;
    private final PesertaKelasTransform mapperPesertaKelas;
    private final MahasiswaRepository mahasiswaRepository;
    private final BatasSksRepository batasSksRepository;
    private final HasilStudiRepository hasilStudiRepository;

    @Transactional
    @Override
    public void save(KrsReqDto dto, HttpServletRequest servletRequest) {
        User user = service.getCurrentUser();

        PeriodeAkademik activePeriode = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new RuntimeException("Tidak ada periode aktif"));

        KrsMahasiswa entity;

        if (krsMahasiswaRepository.existsBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())) {
            entity = krsMahasiswaRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())
                    .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan: " + user.getSiakMahasiswa().getId()));
        } else {
            entity = new KrsMahasiswa();
            entity.setSiakMahasiswa(user.getSiakMahasiswa());
            entity.setSiakPeriodeAkademik(activePeriode);
            entity.setStatus(KrsKey.DRAFT.getLabel());
            entity.setCreatedAt(LocalDateTime.now());
            entity.setIsDeleted(false);
            krsMahasiswaRepository.save(entity);
        }

        List<KrsRincianMahasiswa> rincianMahasiswaList = new ArrayList<>();
        int totalSks = 0;

        for (UUID kelasId : dto.getKelasIds()) {
            KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                    .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan: " + kelasId));

            KrsRincianMahasiswa rincianKrs = mapper.toEntityRincian(dto);
            rincianKrs.setSiakKelasKuliah(kelasKuliah);
            rincianKrs.setSiakKrsMahasiswa(entity);
            rincianKrs.setCreatedAt(LocalDateTime.now());

            boolean isUlang = krsRincianMahasiswaRepository
                    .existsBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(
                            kelasId, user.getSiakMahasiswa().getId());

            rincianKrs.setKategori(isUlang ? KrsKey.ULANG.getLabel() : KrsKey.BARU.getLabel());
            rincianKrs.setIsDeleted(false);

            rincianMahasiswaList.add(rincianKrs);

            totalSks += kelasKuliah.getSiakMataKuliah().getSksTatapMuka()
                    + kelasKuliah.getSiakMataKuliah().getSksPraktikum();
        }

        Integer maxSksYangDiizinkan = getMaxSksYangDiizinkan(user);

        if (totalSks > maxSksYangDiizinkan) {
            throw new RuntimeException("Total SKS yang diambil melebihi batas maksimum: " + maxSksYangDiizinkan);
        }

        krsRincianMahasiswaRepository.saveAll(rincianMahasiswaList);

        entity.setJumlahSksDiambil(totalSks);
        krsMahasiswaRepository.save(entity);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_KRS);
    }

        private Integer getMaxSksYangDiizinkan(User user) {
            BigDecimal ipsTerakhir = hasilStudiRepository
                    .findTopBySiakMahasiswa_IdOrderByCreatedAtDesc(user.getSiakMahasiswa().getId())
                    .map(HasilStudi::getIps)
                    .orElse(BigDecimal.ZERO);

            Jenjang jenjang = user.getSiakMahasiswa().getSiakProgramStudi().getSiakJenjang();

            BatasSks batasSks = batasSksRepository
                    .findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(
                            jenjang, ipsTerakhir, ipsTerakhir
                    ).orElseThrow(() -> new RuntimeException("Batas SKS belum diatur untuk IPS: " + ipsTerakhir));

            return batasSks.getBatasSks();
        }


    @Transactional
    @Override
    public void update(KrsReqDto dto, HttpServletRequest servletRequest) {
        User user = service.getCurrentUser();

        // Ambil periode aktif
        PeriodeAkademik activePeriode = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new RuntimeException("Tidak ada periode aktif"));

        // Ambil entitas KRS Mahasiswa
        KrsMahasiswa krs = krsMahasiswaRepository
                .findBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())
                .orElseThrow(() -> new RuntimeException("KRS Mahasiswa tidak ditemukan: " + user.getSiakMahasiswa().getId()));

        LocalDateTime now = LocalDateTime.now();

        // Update KRS utama
        krs.setSiakPeriodeAkademik(activePeriode);
        krs.setStatus(KrsKey.DRAFT.getLabel());
        krs.setUpdatedAt(now);
        krsMahasiswaRepository.save(krs);

        List<KrsRincianMahasiswa> existingRincians = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_IdAndIsDeletedFalse(krs.getId());
        for (KrsRincianMahasiswa rincian : existingRincians) {
            rincian.setUpdatedAt(now);
        }
        krsRincianMahasiswaRepository.saveAll(existingRincians);

        List<KrsRincianMahasiswa> updatedRincians = new ArrayList<>();
        int totalSks = 0;

        for (UUID kelasId : dto.getKelasIds()) {
            KelasKuliah kelas = kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                    .orElseThrow(() -> new RuntimeException("Kelas tidak ditemukan: " + kelasId));

            Optional<KrsRincianMahasiswa> existingRincianOpt = krsRincianMahasiswaRepository
                    .findBySiakKrsMahasiswa_IdAndSiakKelasKuliah_Id(krs.getId(), kelasId);

            KrsRincianMahasiswa rincian;
            if (existingRincianOpt.isPresent()) {
                rincian = existingRincianOpt.get();
                rincian.setIsDeleted(false);
                rincian.setUpdatedAt(now);
                rincian.setKategori(KrsKey.BARU.getLabel());
            } else {
                rincian = new KrsRincianMahasiswa();
                rincian.setSiakKrsMahasiswa(krs);
                rincian.setSiakKelasKuliah(kelas);
                rincian.setCreatedAt(now);
                rincian.setIsDeleted(false);

                boolean pernahAmbil = krsRincianMahasiswaRepository
                        .existsBySiakKelasKuliah_SiakMataKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_IdNotAndIsDeletedFalse(
                                kelas.getSiakMataKuliah().getId(), user.getSiakMahasiswa().getId(), krs.getId());

                rincian.setKategori(pernahAmbil ? KrsKey.ULANG.getLabel() : KrsKey.BARU.getLabel());
            }

            updatedRincians.add(rincian);

            totalSks += kelas.getSiakMataKuliah().getSksTatapMuka()
                    + kelas.getSiakMataKuliah().getSksPraktikum();

            Integer maxSksYangDiizinkan = getMaxSksYangDiizinkan(user);

            if (totalSks > maxSksYangDiizinkan) {
                throw new RuntimeException("Total SKS yang diambil melebihi batas maksimum: " + maxSksYangDiizinkan);
            }
        }


        krsRincianMahasiswaRepository.saveAll(updatedRincians);

        // Update total SKS
        krs.setJumlahSksDiambil(totalSks);
        krsMahasiswaRepository.save(krs);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KRS);
    }



    @Override
    public Page<KrsResDto> getPaginated(String kelas, Pageable pageable) {
        KrsSpecification specBuilder = new KrsSpecification();
        Specification<KrsRincianMahasiswa> spec = specBuilder.entitySearch(kelas);
        Page<KrsRincianMahasiswa> all = krsRincianMahasiswaRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public KrsMenungguResDto getAllKrsByStatusMenunggu() {
        User user = service.getCurrentUser();
        List<KrsRincianMahasiswa> all = krsMahasiswaRepository.findAllRincianByStatusMenungguAndPeriodeAktifAndMahasiswa(user.getSiakMahasiswa().getId());
        log.info("All Rincians: {}", all.size());
        return mapper.toDtoMenunggu(all);
    }


    @Override
    public void updateStatus(HttpServletRequest servletRequest) {
        User user = service.getCurrentUser();

        PeriodeAkademik firstByStatusActive = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new RuntimeException("Tidak ada periode aktif"));

        KrsMahasiswa entity = krsMahasiswaRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())
                        .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukkan : " + user.getSiakMahasiswa().getId()));

        entity.setSiakMahasiswa(user.getSiakMahasiswa());
        entity.setSiakPeriodeAkademik(firstByStatusActive);
        entity.setStatus(KrsKey.MENUNGGU.getLabel());
        entity.setUpdatedAt(LocalDateTime.now());
        krsMahasiswaRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KRS);
    }

    @Override
    public List<PesertaKelas> getPesertaKelas(UUID kelasId) { // Example method
        kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<KrsRincianMahasiswa> krsRincianMahasiswaList = krsRincianMahasiswaRepository.findPesertaByKelasIdAndIsDeletedFalse(kelasId);

        return mapperPesertaKelas.toDtoList(krsRincianMahasiswaList); // Use the mapper to convert the list
    }

            @Override
            public void addPesertaKelas(UUID kelasId, PesertaKelasReqDto request, HttpServletRequest servletRequest){
                KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                        .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

                PeriodeAkademik activePeriode = periodeAkademikRepository.findFirstByStatusActive()
                        .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tidak ada periode aktif"));

                List<KrsRincianMahasiswa> rincianMahasiswaList = new ArrayList<>();

                for (UUID mahasiswaId : request.getMahasiswaIds()){
                    Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(mahasiswaId).orElseThrow(() -> new RuntimeException("Mahaiswa tidak ditemukan:" + mahasiswaId));

                    KrsMahasiswa entity = mapper.toEntity(request);

                    entity.setSiakMahasiswa(mahasiswa);
                    entity.setSiakPeriodeAkademik(activePeriode);
                    entity.setStatus(KrsKey.DRAFT.getLabel());
                    entity.setUpdatedAt(LocalDateTime.now());
                    entity.setIsDeleted(false);
                    int totalSks = kelasKuliah.getSiakMataKuliah().getSksTatapMuka()
                            + kelasKuliah.getSiakMataKuliah().getSksPraktikum();

                    entity.setJumlahSksDiambil(totalSks);
                    krsMahasiswaRepository.save(entity);

                    KrsRincianMahasiswa rincianMahasiswa = mapper.toEntityRincianPeserta(request);
                    rincianMahasiswa.setSiakKelasKuliah(kelasKuliah);
                    rincianMahasiswa.setSiakKrsMahasiswa(entity);
                    rincianMahasiswa.setCreatedAt(LocalDateTime.now());

                    boolean isUlang = krsRincianMahasiswaRepository
                            .existsBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(
                                    kelasId, mahasiswa.getId());

                    rincianMahasiswa.setKategori(isUlang ? KrsKey.ULANG.getLabel() : KrsKey.BARU.getLabel());
                    rincianMahasiswa.setIsDeleted(false);

                    rincianMahasiswaList.add(rincianMahasiswa);
                }
                krsRincianMahasiswaRepository.saveAll(rincianMahasiswaList);
                service.saveUserActivity(servletRequest, MessageKey.CREATE_KRS);
            }

        @Override
        public void deletePesertaKelas(UUID kelasId, PesertaKelasReqDto request, HttpServletRequest servletRequest) {
            List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                    .findAllBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdInAndIsDeletedFalse(kelasId, request.getMahasiswaIds());

            if (rincianList.isEmpty()) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tidak ada peserta yang ditemukan untuk dihapus");
            }

            for (KrsRincianMahasiswa rincian : rincianList) {
                rincian.setIsDeleted(true);
                rincian.setUpdatedAt(LocalDateTime.now());
                KrsMahasiswa krsMahasiswa = rincian.getSiakKrsMahasiswa();
                if (krsMahasiswa != null) {
                    krsMahasiswa.setIsDeleted(true);
                    krsMahasiswa.setUpdatedAt(LocalDateTime.now());
                    krsMahasiswaRepository.save(krsMahasiswa);
                }
            }
            krsRincianMahasiswaRepository.saveAll(rincianList);
            service.saveUserActivity(servletRequest, MessageKey.DELETE_KRS);
        }

        @Override
        public void pindahKelasPeserta(UUID kelasId, PindahKelasReqDto request, HttpServletRequest servletRequest) {
            List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                    .findAllBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdInAndIsDeletedFalse(kelasId, request.getMahasiswaIds());

            KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(request.getKelasId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas tidak ditemukan"));

            if (rincianList.isEmpty()) {
                throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tidak ada peserta yang ditemukan untuk dipindahkan ke kelas : " + kelasId);
            }

            for (KrsRincianMahasiswa rincian : rincianList) {
                rincian.setIsDeleted(true);
                rincian.setSiakKelasKuliah(kelasKuliah);
                rincian.setUpdatedAt(LocalDateTime.now());
                KrsMahasiswa krsMahasiswa = rincian.getSiakKrsMahasiswa();
                if (krsMahasiswa != null) {
                    krsMahasiswa.setIsDeleted(true);
                    krsMahasiswa.setUpdatedAt(LocalDateTime.now());
                    krsMahasiswaRepository.save(krsMahasiswa);
                }
            }
            krsRincianMahasiswaRepository.saveAll(rincianList);
            service.saveUserActivity(servletRequest, MessageKey.DELETE_KRS);
        }


    @Override
    public List<MengulangResDto> getAllMengulang(UUID mahasiswaId) {
        List<KrsRincianMahasiswa> allRincianKrsMahasiswa = krsRincianMahasiswaRepository
                .findBySiakKrsMahasiswaSiakMahasiswaIdAndIsDeletedFalse(mahasiswaId);

        if (allRincianKrsMahasiswa.isEmpty()) {
            return Collections.emptyList();
        }

        Map<MataKuliah, List<KrsRincianMahasiswa>> attemptsByCourse = allRincianKrsMahasiswa.stream()
                .filter(rincian -> rincian.getSiakKelasKuliah() != null && rincian.getSiakKelasKuliah().getSiakMataKuliah() != null)
                .collect(Collectors.groupingBy(rincian -> rincian.getSiakKelasKuliah().getSiakMataKuliah()));

        List<MengulangResDto> kursusMengulangList = new ArrayList<>();

        for (Map.Entry<MataKuliah, List<KrsRincianMahasiswa>> entry : attemptsByCourse.entrySet()) {
            MataKuliah mataKuliahEntity = entry.getKey();
            List<KrsRincianMahasiswa> courseAttempts = entry.getValue();

            if (courseAttempts.isEmpty()) {
                continue;
            }

            courseAttempts.sort((r1, r2) -> {
                PeriodeAkademik pa1 = r1.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
                PeriodeAkademik pa2 = r2.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
                if (pa1 != null && pa1.getTanggalMulai() != null && pa2 != null && pa2.getTanggalMulai() != null) {
                    return pa2.getTanggalMulai().compareTo(pa1.getTanggalMulai()); // Descending for latest first
                }
                return 0;
            });

            KrsRincianMahasiswa latestAttemptEntity = courseAttempts.get(0); // Get the most recent attempt

            if ("tidak lulus".equalsIgnoreCase(latestAttemptEntity.getStatus())) {
                MengulangResDto mengulangDto = new MengulangResDto();
                mengulangDto.setNamaMataKuliah(mataKuliahEntity.getNamaMataKuliah());
                mengulangDto.setKodeMataKuliah(mataKuliahEntity.getKodeMataKuliah());

                List<PeriodeResDto> periodeResDtoList = courseAttempts.stream()
                        .sorted((r1, r2) -> { // Sort back to chronological for display
                            PeriodeAkademik pa1 = r1.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
                            PeriodeAkademik pa2 = r2.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
                            if (pa1 != null && pa1.getTanggalMulai() != null && pa2 != null && pa2.getTanggalMulai() != null) {
                                return pa1.getTanggalMulai().compareTo(pa2.getTanggalMulai()); // Ascending
                            }
                            return 0;
                        })
                        .map(rincian -> {
                            PeriodeResDto periodeDto = new PeriodeResDto();
                            PeriodeAkademik pa = rincian.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
                            KrsMahasiswa km = rincian.getSiakKrsMahasiswa();

                            periodeDto.setPeriodeAkademik(pa.getNamaPeriode());

                            int sks = mataKuliahEntity.getSksTatapMuka() != null ? mataKuliahEntity.getSksTatapMuka() : 0;
                            if (Boolean.TRUE.equals(mataKuliahEntity.getAdaPraktikum()) && mataKuliahEntity.getSksPraktikum() != null) {
                                sks += mataKuliahEntity.getSksPraktikum();
                            }
                            periodeDto.setSks(sks);

                            Integer semesterAttempt = km.getSemester(); // From KrsMahasiswa
                            periodeDto.setSemester(semesterAttempt != null ? semesterAttempt : 0);

                            periodeDto.setNilai(rincian.getHurufMutu()); // Grade "A", "B", "C", etc.
                            return periodeDto;
                        })
                        .collect(Collectors.toList());

                mengulangDto.setPeriode(periodeResDtoList);
                kursusMengulangList.add(mengulangDto);
            }
        }
        return kursusMengulangList;
    }

    private PeriodeAkademik findPeriodeAkademikByName(String namaPeriode, List<KrsRincianMahasiswa> rincianList) {
        for (KrsRincianMahasiswa rincian : rincianList) {
            if (rincian.getSiakKrsMahasiswa() != null && rincian.getSiakKrsMahasiswa().getSiakPeriodeAkademik() != null &&
                    rincian.getSiakKrsMahasiswa().getSiakPeriodeAkademik().getNamaPeriode().equals(namaPeriode)) {
                return rincian.getSiakKrsMahasiswa().getSiakPeriodeAkademik();
            }
        }
        return null;
    }
}
