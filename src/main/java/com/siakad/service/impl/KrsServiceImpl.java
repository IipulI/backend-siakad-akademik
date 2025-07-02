package com.siakad.service.impl;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PesertaKelasReqDto;
import com.siakad.dto.request.UpdateStatusKrsReqDto;
import com.siakad.dto.response.*;
import com.siakad.dto.request.PindahKelasReqDto;
import com.siakad.dto.transform.helper.EligibleMahasiswaMapper;
import com.siakad.dto.transform.KrsTransform;
import com.siakad.dto.transform.PesertaKelasTransform;
import com.siakad.entity.*;
import com.siakad.entity.service.KelasKuliahSpecification;
import com.siakad.entity.service.KrsSpecification;
import com.siakad.entity.service.MahasiswaSpecification;
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
    private final PembimbingAkademikRepository pembimbingAkademikRepository;
    private final JadwalKuliahRepository jadwalKuliahRepository;
    private final JenjangRepository jenjangRepository;

    private final EligibleMahasiswaMapper eligibleMahasiswaMapper;

    // Mahasiswa
    @Override
    public KrsInfoResDto infoKrs(UUID mahasiswaId){
        Object[] rawResult = mahasiswaRepository.getKrsInfo(mahasiswaId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "RPS detail not found for given mataKuliah and dosen."));

        Object[] actualDataRow = (Object[]) rawResult[0];

        String statusKrs;
        Integer semester;
        Integer batasSks;
        String periodeAkademik;
        String pembimbingAkademik;


        if (actualDataRow[5] == null){
            statusKrs = "Belum Diajukan";
        } else {
            statusKrs = (String) actualDataRow[5];
        }

        if(actualDataRow[4] == null){
            batasSks = (Integer) 21;
        } else {
            batasSks = (Integer) actualDataRow[4];
        }

        semester = (Integer) actualDataRow[1];
        periodeAkademik = (String) actualDataRow[6];
        pembimbingAkademik = (String) actualDataRow[3];

        KrsInfoResDto krsInfoResDto = new KrsInfoResDto();
        krsInfoResDto.setStatusKrs(statusKrs);
        krsInfoResDto.setSemester(semester);
        krsInfoResDto.setBatasSks(batasSks);
        krsInfoResDto.setPeriodeAkademik(periodeAkademik);
        krsInfoResDto.setPembimbingAkademik(pembimbingAkademik);

        return krsInfoResDto;
    }

    @Transactional
    @Override
    public void save(KrsReqDto dto, HttpServletRequest servletRequest) {
        User user = service.getCurrentUser();

        UUID mahasiswaId = user.getSiakMahasiswa().getId();

        PeriodeAkademik activePeriode = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new RuntimeException("Tidak ada periode aktif"));

        KrsMahasiswa entity;

        krsMahasiswaRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, activePeriode.getId()).orElseThrow(
                         ()-> new ApplicationException(ExceptionType.BAD_REQUEST, "Kamu sudah mengisi untuk periode ini")
                 );

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
                    .findHasilStudiBySiakMahasiswa_IdOrderBySemesterDesc(user.getSiakMahasiswa().getId())
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
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademikIdAndIsDeletedFalse(user.getSiakMahasiswa().getId(), activePeriode.getId())
                .orElseThrow(() -> new RuntimeException("KRS Mahasiswa tidak ditemukan: " + user.getSiakMahasiswa().getId()));

        LocalDateTime now = LocalDateTime.now();

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

    @Transactional
    @Override
    public void deleteMultiple(KrsReqDto reqDto, HttpServletRequest httpServletRequest) {
        User user = service.getCurrentUser();

        KrsMahasiswa krs = krsMahasiswaRepository
                .findBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())
                .orElseThrow(() -> new RuntimeException("KRS Mahasiswa tidak ditemukan: " + user.getSiakMahasiswa().getId()));

        LocalDateTime now = LocalDateTime.now();

        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_IdAndSiakKelasKuliah_IdInAndIsDeletedFalse(krs.getId(), reqDto.getKelasIds());

        for (KrsRincianMahasiswa rincian : rincianList) {
            rincian.setIsDeleted(true);
            rincian.setUpdatedAt(now);
        }

        krsRincianMahasiswaRepository.saveAll(rincianList);

        service.saveUserActivity(httpServletRequest, MessageKey.DELETE_KRS);
    }


    @Override
    public Page<KrsResDto> getPaginated(String mataKuliah, Pageable pageable) {
        KrsSpecification specBuilder = new KrsSpecification();

        User user = service.getCurrentUser();
        Mahasiswa mahasiswa = user.getSiakMahasiswa();

        List<String> semesters = switch (mahasiswa.getSemester()) {
            case 1, 3, 5, 7, 9, 11, 13 -> Arrays.asList("1", "3", "5", "7", "9", "11", "13");
            case 2, 4, 6, 8, 10, 12, 14 -> Arrays.asList("2", "4", "6", "8", "10", "12");
            default -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14");
        };

        Specification<KrsRincianMahasiswa> spec = specBuilder.entitySearch(mataKuliah, semesters, mahasiswa.getId());
        Page<KrsRincianMahasiswa> all = krsRincianMahasiswaRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public Page<KrsResDto> getPaginatedKelas(String mataKuliah, Pageable pageable) {

        String programStudi = service.getCurrentUser().getSiakMahasiswa().getSiakProgramStudi().getNamaProgramStudi();

        PeriodeAkademik periodeAktif = periodeAkademikRepository.findFirstByStatusActive().orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tidak ada periode Aktif"));
        KelasKuliahSpecification specBuilder = new KelasKuliahSpecification();
        Specification<KelasKuliah> spec = specBuilder.entitySearchKelas(mataKuliah, programStudi, periodeAktif.getNamaPeriode());
        Page<KelasKuliah> all = kelasKuliahRepository.findAll(spec, pageable);
        return all.map(mapper::toDtoKelas);
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
        entity.setStatus(KrsKey.DIAJUKAN.getLabel());
        entity.setUpdatedAt(LocalDateTime.now());
        krsMahasiswaRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KRS);
    }

    @Override
    public List<PesertaKelas> getPesertaKelas(UUID kelasId) {
        kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<KrsRincianMahasiswa> krsRincianMahasiswaList = krsRincianMahasiswaRepository.findPesertaByKelasIdAndIsDeletedFalse(kelasId);

        return mapperPesertaKelas.toDtoList(krsRincianMahasiswaList); // Use the mapper to convert the list
    }

    @Override
    public List<EligiblePesertaKelasDto> getEligiblePesertaKelas(UUID kelasId, String nama, String periodeAkademik, String sistemKuliah){
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        // total sks matakuliah ini
        Integer sksMataKuliah = kelasKuliah.getSiakMataKuliah().getSksPraktikum() + kelasKuliah.getSiakMataKuliah().getSksTatapMuka();

        // list mahasiswa di prodi ini
        MahasiswaSpecification specBuilder = new MahasiswaSpecification();
        Specification<Mahasiswa> spec = specBuilder.entitySearch(nama, null, periodeAkademik, sistemKuliah, null, null, kelasKuliah.getSiakProgramStudi().getNamaProgramStudi(),  null, null, null, null, null, null, null);
        List<Mahasiswa> eligibleMahasiswa = mahasiswaRepository.findAll(spec);

        // list mahasiswa registered di kelas ini
        Set<UUID> registeredMahasiswaIds = krsRincianMahasiswaRepository.findRegisteredMahasiswaIdsByKelasId(kelasId);

        // get periode
        String kodePeriodeSebelumnya;
        PeriodeAkademik periodeAktif = periodeAkademikRepository.findFirstByStatusActive()
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode aktif"));

        if (periodeAktif.getKodePeriode().charAt(periodeAktif.getKodePeriode().length() - 1) == '1') {
            String tahunAktif = periodeAktif.getKodePeriode().substring(0, 4);
            Integer tahunSebelumnya = Integer.parseInt(tahunAktif) - 1;
            kodePeriodeSebelumnya = tahunSebelumnya + "2";
        } else {
            String tahunAktif = periodeAktif.getKodePeriode();
            int tahunSebelumnya = Integer.parseInt(tahunAktif) - 1;
            kodePeriodeSebelumnya = Integer.toString(tahunSebelumnya);
        }

        PeriodeAkademik periodeSebelumnya = periodeAkademikRepository.findByKodePeriodeAndIsDeletedFalse(kodePeriodeSebelumnya)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode tidak ditemukan"));

        return eligibleMahasiswa.stream()
                // Filter mahasiswa yang belum terdaftar di kelas ini
                .filter(mahasiswa -> !registeredMahasiswaIds.contains(mahasiswa.getId()))
                .map(mahasiswa -> {
                    // Kalkulasi batas SKS dinamis
                    Integer batasSks = determineBatasSks(mahasiswa, periodeSebelumnya);

                    // Ambil SKS yang sudah diambil di periode aktif
                    Integer sksDiambil = krsMahasiswaRepository
                            .findBySiakMahasiswaAndSiakPeriodeAkademik(mahasiswa, periodeAktif)
                            .map(KrsMahasiswa::getJumlahSksDiambil)
                            .orElse(0);

                    // Pengecekan akhir kelayakan
                    if ((sksDiambil + sksMataKuliah) > batasSks) {
                        return null; // Tidak eligible
                    }

                    // Mapping ke DTO menggunakan Mapper terpisah
                    return eligibleMahasiswaMapper.toDto(mahasiswa, batasSks, sksDiambil);
                })
                .filter(Objects::nonNull) // Hapus yang tidak eligible
                .collect(Collectors.toList());

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

            if ("Tidak Lulus".equalsIgnoreCase(latestAttemptEntity.getStatus())) {
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

                            Integer semesterAttempt = km.getSiakMahasiswa().getSemester();
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

    @Override
    public List<MengulangResDto> getAllMengulangByPeriode(UUID mahasiswaId, UUID periodeId) {
        List<KrsRincianMahasiswa> allRincianKrsMahasiswa = krsRincianMahasiswaRepository
                .findBySiakKrsMahasiswaSiakMahasiswa_IdAndSiakKrsMahasiswaSiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, periodeId);

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

            if ("Tidak Lulus".equalsIgnoreCase(latestAttemptEntity.getStatus())) {
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

                            Integer semesterAttempt = km.getSiakMahasiswa().getSemester();
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

    @Override
    public void deleteKrs(UUID id, HttpServletRequest servletRequest) {
        KrsRincianMahasiswa krsRincianMahasiswa = krsRincianMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND));

        krsRincianMahasiswa.setIsDeleted(true);
        krsRincianMahasiswaRepository.save(krsRincianMahasiswa);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_KRS);
    }

    @Override
    public List<FinalisasiMkDto> getAllFinalisasiMk(UUID mahasiswaId) {
        List<KrsRincianMahasiswa> allRincian = krsRincianMahasiswaRepository.findAllActiveByMahasiswaId(mahasiswaId);

        List <FinalisasiMkDto> result = new ArrayList<>();
        for(KrsRincianMahasiswa krs : allRincian){

            FinalisasiMkDto dto = new FinalisasiMkDto();
            dto.setPeriodeAkademik(krs.getSiakKrsMahasiswa().getSiakPeriodeAkademik().getNamaPeriode());
            String tahunKurikulum = krs.getSiakKelasKuliah().getSiakMataKuliah().getSiakTahunKurikulum().getTahun();
            dto.setKurikulum(tahunKurikulum);
            dto.setKodeMataKuliah(krs.getSiakKelasKuliah().getSiakMataKuliah().getKodeMataKuliah());
            dto.setNamaMatakuliah(krs.getSiakKelasKuliah().getSiakMataKuliah().getNamaMataKuliah());
            Integer sks = krs.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum() + krs.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka();
            dto.setSks(sks);
            dto.setOpsiMataKuliah(krs.getSiakKelasKuliah().getSiakMataKuliah().getOpsiMataKuliah());
            dto.setGrade(krs.getHurufMutu());
            String status = krs.getStatus();
            dto.setStatus(status);
            dto.setDipakai(tahunKurikulum != null && !tahunKurikulum.isEmpty());

            if(status.equalsIgnoreCase("Lulus")){
                dto.setTranskip(true);
            } else if(status.equalsIgnoreCase("Tidak Lulus")){
                dto.setTranskip(false);
            }
            result.add(dto);
        }
        return result;
    }

    @Override
    public List<StatusSemesterDto> getStatusSemester(UUID mahasiswaId) {
        List<HasilStudi> hasilStudis = hasilStudiRepository.findAllBySiakMahasiswa_IdAndIsDeletedFalse(mahasiswaId);

        List<StatusSemesterDto> result = new ArrayList<>();

        for (HasilStudi hasilStudi : hasilStudis) {

            UUID mshId = hasilStudi.getSiakMahasiswa().getId();
            PembimbingAkademik pembimbingAkademik = pembimbingAkademikRepository.findBySiakMahasiswa_IdAndIsDeletedFalse(mshId)
                    .orElseThrow(() -> new RuntimeException("PembimbingAkademik not found"));

            StatusSemesterDto dto = new StatusSemesterDto();
            dto.setKodePeriode(hasilStudi.getSiakPeriodeAkademik().getKodePeriode());
            dto.setSemester(hasilStudi.getSemester());
            dto.setStatus(hasilStudi.getSiakMahasiswa().getStatusMahasiswa());
            dto.setSks(hasilStudi.getSksDiambil());
            dto.setSksTotal(hasilStudi.getSksDiambil());
            dto.setSksTempuh(hasilStudi.getSksDiambil());
            dto.setSksLulus(hasilStudi.getSksLulus());
            dto.setIpk(hasilStudi.getIpk());
            dto.setIps(hasilStudi.getIps());
            dto.setDosen(pembimbingAkademik.getSiakDosen().getNama());

            result.add(dto);
        }
        return result;
    }

    @Override
    public RiwayatKrsDto getRiwayatKrs(UUID mahasiswaId, String namaPeriode ) {
        List<KrsRincianMahasiswa> krsMahasiswaList = krsRincianMahasiswaRepository.findAllActiveByMahasiswaIdAndPeriodeAkademik(mahasiswaId, namaPeriode);

        List<KrsDto> result = new ArrayList<>();
        int totalSks = 0;
        for (KrsRincianMahasiswa krs : krsMahasiswaList) {
            KrsDto krsDto = new KrsDto();
            krsDto.setKodeMataKuliah(krs.getSiakKelasKuliah().getSiakMataKuliah().getKodeMataKuliah());
            krsDto.setNamaMataKuliah(krs.getSiakKelasKuliah().getSiakMataKuliah().getNamaMataKuliah());
            krsDto.setKelas(krs.getSiakKelasKuliah().getNama());
            int sks = krs.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum() + krs.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka();
            krsDto.setSks(sks);
            totalSks += sks;
            UUID kelasId = krs.getSiakKelasKuliah().getId();

            JadwalKuliah jadwalKuliah = jadwalKuliahRepository.findBySiakKelasKuliahIdAndIsDeletedFalse(kelasId)
                    .orElseThrow(() -> new RuntimeException("JadwalKuliah not found"));

            String jam = jadwalKuliah.getJamMulai() + " - " + jadwalKuliah.getJamSelesai();
            krsDto.setJam(jam);
            krsDto.setRuangan(jadwalKuliah.getSiakRuangan().getNamaRuangan());

            Dosen dosen = jadwalKuliah.getSiakDosen();
            String namaDosen = (dosen != null) ? dosen.getNama() : "-";
            krsDto.setDosenPengajar(namaDosen);

            result.add(krsDto);
        }

        RiwayatKrsDto dto = new RiwayatKrsDto();
        dto.setKrs(result);

        dto.setBatasSks(getBatasSks(mahasiswaId));
        dto.setTotalSks(totalSks);
        return dto;
    }

    @Override
    public List<SuntingKrsResDto> getSuntingKrs(UUID mahasiswaId, String namaPeriode) {
        List<Object[]> rawResults = krsRincianMahasiswaRepository.getSuntingFindByMahasiswaIdAndNamaPeriode(mahasiswaId, namaPeriode);

        if (rawResults.isEmpty()) {
            throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "KRS tidak ditemukan untuk mahasiswa " + mahasiswaId + " pada periode " + namaPeriode);
        }

        List<SuntingKrsResDto> dtoList = new ArrayList<>();

        for (Object[] actualResult : rawResults) {
            // Create a new DTO for each row
            SuntingKrsResDto dto = new SuntingKrsResDto();

            // Index 0: tk.tahun (INTEGER in DB -> Integer in Java)
            if (actualResult[0] instanceof Integer) {
                dto.setKurikulum((Integer) actualResult[0]);
            } else if (actualResult[0] instanceof String) { // Defensive check, though less likely if DB type is INTEGER
                dto.setKurikulum(Integer.parseInt((String) actualResult[0]));
            }

            // Index 1: mk.kode_mata_kuliah (VARCHAR -> String)
            dto.setKodeMataKuliah((String) actualResult[1]);

            // Index 2: mk.nama_mata_kuliah (VARCHAR -> String)
            dto.setNamaMataKuliah((String) actualResult[2]);

            // Index 4: kk.nama (VARCHAR -> String)
            dto.setNamaKelas((String) actualResult[4]);

            // Calculate SKS (Index 5: mk.sks_tatap_muka, Index 6: mk.sks_praktikum)
            Integer sksTatapMuka = (Integer) actualResult[5];
            Integer sksPraktikum = (Integer) actualResult[6];
            dto.setSks((sksTatapMuka != null ? sksTatapMuka : 0) + (sksPraktikum != null ? sksPraktikum : 0));

            // Index 7: rkm.nilai (NUMERIC -> BigDecimal)
            dto.setNilaiNumerik((BigDecimal) actualResult[7]);

            // Index 8: sp_student.huruf_mutu (VARCHAR -> String)
            dto.setNilaiHuruf((String) actualResult[8]);

            // Index 9: sp_student.angka_mutu (NUMERIC -> BigDecimal)
            dto.setNilaiMutu((BigDecimal) actualResult[9]);

            // Index 11: is_passed (BOOLEAN -> Boolean) - can be null
            dto.setLulus((Boolean) actualResult[11]); // Will be true, false, or null

            // Add the populated DTO to the list
            dtoList.add(dto);
        }

        return dtoList;
    }


    private static final String STATUS_DIAJUKAN = "Diajukan";
    private static final String STATUS_DISETUJUI = "Disetujui";
    private static final String STATUS_DITOLAK = "Ditolak";

    @Override
    public void updateStatusKrsSetuju(UpdateStatusKrsReqDto request, HttpServletRequest servletRequest){
        processBulkUpdate(request, servletRequest, STATUS_DISETUJUI);
    }

    @Override
    public void updateStatusKrsTolak(UpdateStatusKrsReqDto request, HttpServletRequest servletRequest){
        processBulkUpdate(request, servletRequest, STATUS_DITOLAK);
    }

    @Override
    public KrsMenungguResDto getDetailKrsMahasiswa(UUID mahasiswaId) {
        List<KrsRincianMahasiswa> all = krsMahasiswaRepository.findAllRincianByStatusMenungguAndPeriodeAktifAndMahasiswa(mahasiswaId);
        return mapper.toDtoMenunggu(all);
    }

    private void processBulkUpdate(UpdateStatusKrsReqDto dto, HttpServletRequest servletRequest, String newStatus) {
        List<UUID> requestedIds = dto.getMahasiswaIds();
        UUID periodeId = dto.getPeriodeAkademikId();

        // Find all KRS that are eligible for this bulk update
        List<KrsMahasiswa> krsToUpdate = krsMahasiswaRepository.findBySiakMahasiswa_IdInAndSiakPeriodeAkademik_IdAndStatus(
                requestedIds, periodeId, STATUS_DIAJUKAN
        );

        // Add one more log to see the result of the query
        System.out.println("Query finished. Found " + krsToUpdate.size() + " records to update.");


        for (KrsMahasiswa krs : krsToUpdate) {
            krs.setStatus(newStatus);
        }

        krsMahasiswaRepository.saveAll(krsToUpdate);
        if (Objects.equals(newStatus, STATUS_DISETUJUI)){
            service.saveUserActivity(servletRequest, MessageKey.APPROVE_KRS);
        } else if (Objects.equals(newStatus, STATUS_DITOLAK)){
            service.saveUserActivity(servletRequest, MessageKey.RETURN_KRS);
        }
    }

    private Integer getBatasSks(UUID mahasiswaId) {
        BigDecimal ipsTerakhir = hasilStudiRepository
                .findHasilStudiBySiakMahasiswa_IdOrderBySemesterDesc(mahasiswaId)
                .map(HasilStudi::getIps)
                .orElse(BigDecimal.ZERO);

        Jenjang jenjang = mahasiswaRepository.findJenjangByMahasiswaId(mahasiswaId);

        BatasSks batasSks = batasSksRepository
                .findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(
                        jenjang, ipsTerakhir, ipsTerakhir
                ).orElseThrow(() -> new RuntimeException("Batas SKS belum diatur untuk IPS: " + ipsTerakhir));

        return batasSks.getBatasSks();
    }

    private Integer determineBatasSks(Mahasiswa mahasiswa, PeriodeAkademik periodeSebelumnya) {
        int DEFAULT_BATAS_SKS = 21;

        // 1. Cari data hasil studi terakhir (semester tertinggi)
        Optional<HasilStudi> hasilStudiTerbaru = hasilStudiRepository
                .findLatestByMahasiswa(mahasiswa.getId());

        // 2. Jika tidak ada data sama sekali, berarti mahasiswa baru
        if (hasilStudiTerbaru.isEmpty()) {
            return DEFAULT_BATAS_SKS; // return 21
        }

        // 3. Jika ada, ambil IPS dan Jenjang-nya
        BigDecimal ips = hasilStudiTerbaru.get().getIps();
        Jenjang jenjang = mahasiswa.getSiakProgramStudi().getSiakJenjang();

        // 4. Cari aturan batas SKS di tabel BatasSks
        return batasSksRepository.findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(jenjang, ips, ips)
                .map(BatasSks::getBatasSks)
                .orElse(DEFAULT_BATAS_SKS);
    }
}
