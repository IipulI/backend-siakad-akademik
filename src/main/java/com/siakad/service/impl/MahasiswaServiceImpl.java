    package com.siakad.service.impl;

    import com.siakad.dto.request.EditKeluargaMahasiswaReqDto;
    import com.siakad.dto.request.EditMahasiswaReqDto;
    import com.siakad.dto.request.KeluargaMahasiswaReqDto;
    import com.siakad.dto.request.MahasiswaReqDto;
    import com.siakad.dto.response.MahasiswaChartDto;
    import com.siakad.dto.response.MahasiswaResDto;
    import com.siakad.dto.response.ProfileInfo;
    import com.siakad.dto.response.chart.*;
    import com.siakad.dto.transform.MahasiswaTransform;
    import com.siakad.entity.*;
    import com.siakad.entity.service.MahasiswaSpecification;
    import com.siakad.enums.ExceptionType;
    import com.siakad.enums.MessageKey;
    import com.siakad.enums.RoleType;
    import com.siakad.exception.ApplicationException;
    import com.siakad.repository.*;
    import com.siakad.service.MahasiswaService;
    import com.siakad.service.UserActivityService;
    import com.siakad.util.FileUtils;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.transaction.Transactional;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.domain.Specification;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.UUID;
    import java.util.stream.Collectors;
    import java.util.stream.Stream;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class MahasiswaServiceImpl implements MahasiswaService {

        private final MahasiswaTransform mapper;
        private final MahasiswaRepository mahasiswaRepository;
        private final UserRoleRepository userRoleRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final UserActivityService service;
        private final ProgramStudiRepository programStudiRepository;
        private final PeriodeAkademikRepository periodeAkademikRepository;
        private final PembimbingAkademikRepository pembimbingAkademikRepository;

        private final HasilStudiRepository hasilStudiRepository;
        private final KrsMahasiswaRepository krsMahasiswaRepository;
        private final KrsRincianMahasiswaRepository krsRincianRepository;

        // In a real app, these would come from the database (e.g., from ProgramStudi)
        private static final int SKS_BATAS_LULUS = 144;
        private static final BigDecimal IP_MINIMUM = new BigDecimal("2.50");
        private final KeluargaMahasiswaRepository keluargaMahasiswaRepository;

        @Transactional
        @Override
        public MahasiswaResDto create(MahasiswaReqDto request,
                                      List<KeluargaMahasiswaReqDto> requestKeluarga,
                                      MultipartFile fotoProfil,
                                      MultipartFile ijazahSekolah,
                                      HttpServletRequest servletRequest) throws IOException {

            var programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan"));

            if (mahasiswaRepository.existsByNpm(request.getNpm())) {
                throw new ApplicationException(ExceptionType.NPM_ALREADY_EXISTS, "NPM sudah digunakan");
            }

            if (mahasiswaRepository.existsByEmailPribadi(request.getEmailPribadi())) {
                throw new ApplicationException(ExceptionType.EMAIL_ALREADY_EXISTS, "Email sudah digunakan");
            }

            String password = request.getTanggalLahir().toString().replace("-", "");
            User user = createUserWithRole(request.getNpm(), request.getEmailPribadi(), password, RoleType.MAHASISWA);

            Mahasiswa mahasiswa = mapper.toEntity(request);

            if (fotoProfil != null && !fotoProfil.isEmpty()) {
                mahasiswa.setFotoProfil(FileUtils.compress(fotoProfil.getBytes()));
            }

            if (ijazahSekolah != null && !ijazahSekolah.isEmpty()) {
                mahasiswa.setIjazahSekolah(FileUtils.compress(ijazahSekolah.getBytes()));
            }

            mahasiswa.setEmailPribadi(user.getEmail());
            mahasiswa.setSiakProgramStudi(programStudi);
            mahasiswa.setKurikulum(request.getKurikulum());
            mahasiswa.setSiakUser(user);
            mahasiswa.setIsDeleted(false);

            mahasiswaRepository.save(mahasiswa);

            if (requestKeluarga != null && !requestKeluarga.isEmpty()) {
                for (KeluargaMahasiswaReqDto keluargaDto : requestKeluarga) {
                    KeluargaMahasiswa keluargaMahasiswa = mapper.toEntity(keluargaDto);
                    keluargaMahasiswa.setIsDeleted(false);
                    keluargaMahasiswa.setSiakMahasiswa(mahasiswa);
                    keluargaMahasiswaRepository.save(keluargaMahasiswa);
                }
            }

            service.saveUserActivity(servletRequest, MessageKey.CREATE_MAHASISWA);

            return mapper.toDto(mahasiswa);
        }



        @Override
        public Page<MahasiswaResDto> getPaginated(
                String keyword,
                String programStudi,
                String jenisPendaftaran,
                String kelasPerkuliahan,
                String angkatan,
                String jalurPendaftaran,
                String statusMahasiswa,
                String gelombang,
                String jenisKelamin,
                String sistemKuliah,
                String kurikulum,
                String periodeMasuk,
                String periodeKeluar,
                int page, int size) {

            MahasiswaSpecification specBuilder = new MahasiswaSpecification();
            Specification<Mahasiswa> spec = specBuilder.entitySearch(
                    keyword, null, periodeMasuk, sistemKuliah, angkatan, null, programStudi, jenisPendaftaran, jalurPendaftaran,
                    statusMahasiswa, gelombang, jenisKelamin, kurikulum, periodeKeluar
            );

            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Mahasiswa> all = mahasiswaRepository.findAll(spec, pageable);

            return all.map(mahasiswa -> {
                MahasiswaResDto dto = mapper.toDto(mahasiswa);
                ProfileInfo profileInfo = getProfileInfoByMahasiswa(mahasiswa.getId());
                dto.setSks(profileInfo.getTotalSks());
                dto.setIpk(profileInfo.getIpk());

                return dto;
            });
        }


        @Override
        public byte[] getFotoProfil(UUID id) {
            Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id);
            return FileUtils.decompress(mahasiswa.get().getFotoProfil());
        }

        @Override
        public byte[] getIjazahSekolah(UUID id) {
            Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id);
            return FileUtils.decompress(mahasiswa.get().getIjazahSekolah());
        }

        @Override
        public MahasiswaResDto getOne(UUID id) {
            Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND, ExceptionType.USER_NOT_FOUND.getFormattedMessage("With id: " + id)));

            List<KeluargaMahasiswa> keluargaAktif = mahasiswa.getKeluarga()
                    .stream()
                    .filter(k -> !Boolean.TRUE.equals(k.getIsDeleted()))
                    .toList();
            mahasiswa.setKeluarga(keluargaAktif);

            return mapper.toDto(mahasiswa);
        }


        @Override
        @Transactional
        public MahasiswaResDto update(UUID id,
                                      MultipartFile fotoProfil,
                                      MultipartFile ijazahSekolah,
                                      EditMahasiswaReqDto request,
                                      HttpServletRequest servletRequest) throws IOException {

            var programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan"));

            Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "mahasiswa tidak valid dengan id: " + id));

            if (!mahasiswa.getNpm().equals(request.getNpm()) &&
                    mahasiswaRepository.existsByNpm(request.getNpm())) {
                throw new ApplicationException(ExceptionType.NPM_ALREADY_EXISTS, "NPM sudah digunakan");
            }

            if (!mahasiswa.getEmailPribadi().equals(request.getEmailPribadi()) &&
                    mahasiswaRepository.existsByEmailPribadi(request.getEmailKampus())) {
                throw new ApplicationException(ExceptionType.EMAIL_ALREADY_EXISTS, "Email sudah digunakan");
            }

            if (!mahasiswa.getNpm().equals(request.getNpm()) && !mahasiswa.getEmailKampus().equals(request.getEmailKampus())) {
                String password = request.getTanggalLahir().toString().replace("-", "");
                User newUser = createUserWithRole(request.getNpm(), request.getEmailKampus(), password, RoleType.MAHASISWA);
                mahasiswa.setSiakUser(newUser);
            }

            mapper.toEntity(request, mahasiswa);
            mahasiswa.setUpdatedAt(LocalDateTime.now());

            if (fotoProfil != null && !fotoProfil.isEmpty()) {
                mahasiswa.setFotoProfil(FileUtils.compress(fotoProfil.getBytes()));
            }

            if (ijazahSekolah != null && !ijazahSekolah.isEmpty()) {
                mahasiswa.setIjazahSekolah(FileUtils.compress(ijazahSekolah.getBytes()));
            }

            mahasiswa.setSiakProgramStudi(programStudi);
            mahasiswa.setKurikulum(request.getKurikulum());

            // ========== PERLAKUAN UNTUK DATA KELUARGA ==========
            List<EditKeluargaMahasiswaReqDto> keluargaList = request.getKeluargaMahasiswaList();

            if (keluargaList == null || keluargaList.isEmpty()) {
                // ❌ Tidak ada yang dikirim → soft delete semua keluarga
                List<KeluargaMahasiswa> existingKeluarga = keluargaMahasiswaRepository.findAllBySiakMahasiswaIdAndIsDeletedFalse(mahasiswa.getId());
                for (KeluargaMahasiswa keluarga : existingKeluarga) {
                    keluarga.setIsDeleted(true);
                    keluargaMahasiswaRepository.save(keluarga);
                }
            } else {
                for (EditKeluargaMahasiswaReqDto keluargaDto : keluargaList) {
                    if (keluargaDto.getId() != null) {
                        KeluargaMahasiswa keluarga = keluargaMahasiswaRepository.findByIdAndIsDeletedFalse(keluargaDto.getId())
                                .orElseThrow(() -> new RuntimeException("Keluarga tidak ditemukan dengan ID: " + keluargaDto.getId()));

                        if (Boolean.TRUE.equals(keluargaDto.getIsDeleted())) {
                            // ✅ Soft delete per item
                            keluarga.setIsDeleted(true);
                        } else {
                            // ✅ Update data
                            mapper.toEntity(keluargaDto, keluarga);
                        }
                        keluargaMahasiswaRepository.save(keluarga);
                    } else {
                        // ✅ Tambah baru
                        KeluargaMahasiswa keluargaBaru = mapper.toEntityKeluarga(keluargaDto);
                        keluargaBaru.setSiakMahasiswa(mahasiswa);
                        keluargaBaru.setIsDeleted(false);
                        keluargaMahasiswaRepository.save(keluargaBaru);
                    }
                }
            }

            mahasiswaRepository.save(mahasiswa);
            service.saveUserActivity(servletRequest, MessageKey.UPDATE_MAHASISWA);

            return mapper.toDto(mahasiswa);
        }



        @Override
        public void delete(UUID id, HttpServletRequest servletRequest) {
            Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(id)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "mahasiswa tidak valid dengan id: " + id
                    ));

            mahasiswa.setIsDeleted(true);
            Mahasiswa save = mahasiswaRepository.save(mahasiswa);
            service.saveUserActivity(servletRequest, MessageKey.DELETE_MAHASISWA);
            mapper.toDto(save);
        }

        @Override
        public User createUserWithRole(String username,
                                       String email,
                                       String password,
                                       RoleType roleType) {
            Role role = roleRepository.findByNama(roleType)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.ROLE_NOT_FOUND, "Role tidak valid"));

            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            userRepository.save(user);

            UserRole userRole = UserRole.builder()
                    .id(new UserRole.UserRoleId(user.getId(), role.getId()))
                    .build();
            userRoleRepository.save(userRole);
            return user;
        }

        @Override
        public MahasiswaChartDto getDashboardAkademik(UUID mahasiswaId){
            List<HasilStudi> riwayatHasilStudi = hasilStudiRepository.findBySiakMahasiswaIdOrderBySemesterAsc(mahasiswaId);
            List<Object[]> sksDiambilData = krsMahasiswaRepository.findSksDiambilPerSemester(mahasiswaId);
            List<Object[]> distribusiNilaiData = krsRincianRepository.findGradeDistributionBySks(mahasiswaId);

            PerkuliahanChartDto perkuliahanChart = buildPerkuliahanChart(sksDiambilData);
            ProgresSksChartDto progresSksChart = buildProgresSksChart(riwayatHasilStudi);
            IndeksPrestasiChartDto indeksPrestasiChart = buildIndeksPrestasiChart(riwayatHasilStudi);
            SksTempuhChartDto sksTempuhChart = buildSksTempuhChart(riwayatHasilStudi, sksDiambilData);
            DistribusiNilaiChartDto distribusiNilaiChart = buildDistribusiNilaiChart(distribusiNilaiData);


            return MahasiswaChartDto.builder()
                    .perkuliahan(perkuliahanChart)
                    .progresSks(progresSksChart)
                    .sksTempuh(sksTempuhChart)
                    .indeksPrestasi(indeksPrestasiChart)
                    .distribusiNilai(distribusiNilaiChart)
                    .build();
        }

        @Transactional
        @Override
        public ProfileInfo getProfileInfo() {
            User currentUser = service.getCurrentUser();
            UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

            PeriodeAkademik periodeAktif = periodeAkademikRepository.findByStatusActive();

            List<PembimbingAkademik> pembimbingList =
                    pembimbingAkademikRepository
                            .findAllBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(
                                    mahasiswaId, periodeAktif.getId());
            PembimbingAkademik pembimbingAkademik =
                    pembimbingList.isEmpty() ? null : pembimbingList.get(0);

            List<KrsMahasiswa> daftarKrs = krsMahasiswaRepository
                    .findAllBySiakMahasiswa_IdAndIsDeletedFalse(mahasiswaId);

            int totalSks = 0;
            int sksLulus = 0;
            BigDecimal totalBobot = BigDecimal.ZERO;
            BigDecimal totalBobotLulus = BigDecimal.ZERO;

            for (KrsMahasiswa krs : daftarKrs) {
                List<KrsRincianMahasiswa> rincianList =
                        krsRincianRepository.findAllBySiakKrsMahasiswa_IdAndIsDeletedFalse(krs.getId());

                for (KrsRincianMahasiswa rincian : rincianList) {
                    Integer sksPraktikum = rincian.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum();
                    Integer sksTatapMuka = rincian.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka();

                    int sks = (sksPraktikum != null ? sksPraktikum : 0) + (sksTatapMuka != null ? sksTatapMuka : 0);
                    if (sks == 0) continue;

                    totalSks += sks;
                    BigDecimal angkaMutu = rincian.getAngkaMutu();

                    if (angkaMutu != null) {
                        BigDecimal bobot = angkaMutu.multiply(BigDecimal.valueOf(sks));
                        totalBobot = totalBobot.add(bobot);

                        if (rincian.getHurufMutu() != null &&
                                !rincian.getHurufMutu().equalsIgnoreCase("E")) {
                            sksLulus += sks;
                            totalBobotLulus = totalBobotLulus.add(bobot);
                        }
                    }
                }
            }

            double ipk = totalSks == 0 ? 0 : totalBobot.divide(BigDecimal.valueOf(totalSks), 2, RoundingMode.HALF_UP).doubleValue();
            double ipkLulus = sksLulus == 0 ? 0 : totalBobotLulus.divide(BigDecimal.valueOf(sksLulus), 2, RoundingMode.HALF_UP).doubleValue();

            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setNim(currentUser.getSiakMahasiswa().getNpm());
            profileInfo.setNamaMahasiswa(currentUser.getSiakMahasiswa().getNama());
            profileInfo.setProgramStudi(currentUser.getSiakMahasiswa().getSiakProgramStudi().getNamaProgramStudi());
            profileInfo.setStatusMahasiwa(currentUser.getSiakMahasiswa().getStatusMahasiswa());
            profileInfo.setAngkatan(currentUser.getSiakMahasiswa().getAngkatan());
            profileInfo.setTahunKurikulum(currentUser.getSiakMahasiswa().getKurikulum());
            profileInfo.setSemester(currentUser.getSiakMahasiswa().getSemester());
            profileInfo.setPembimbingAkademik(
                    pembimbingAkademik != null ? pembimbingAkademik.getSiakDosen().getNama() : "-"
            );

            profileInfo.setTotalSks(totalSks);
            profileInfo.setSksLulus(sksLulus);
            profileInfo.setIpkLulus(ipkLulus);
            profileInfo.setIpk(ipk);

            return profileInfo;
        }

        @Transactional
        @Override
        public ProfileInfo getProfileInfoByMahasiswa(UUID mahasiswaId) {

            PeriodeAkademik periodeAktif = periodeAkademikRepository.findByStatusActive();

            Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(mahasiswaId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mahasiswa tidak ditemukkan"));

            List<PembimbingAkademik> pembimbingList =
                    pembimbingAkademikRepository
                            .findAllBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(
                                    mahasiswaId, periodeAktif.getId());
            PembimbingAkademik pembimbingAkademik =
                    pembimbingList.isEmpty() ? null : pembimbingList.get(0);

            List<KrsMahasiswa> daftarKrs = krsMahasiswaRepository
                    .findAllBySiakMahasiswa_IdAndIsDeletedFalse(mahasiswaId);

            int totalSks = 0;
            int sksLulus = 0;
            BigDecimal totalBobot = BigDecimal.ZERO;
            BigDecimal totalBobotLulus = BigDecimal.ZERO;

            for (KrsMahasiswa krs : daftarKrs) {
                List<KrsRincianMahasiswa> rincianList =
                        krsRincianRepository.findAllBySiakKrsMahasiswa_IdAndIsDeletedFalse(krs.getId());

                for (KrsRincianMahasiswa rincian : rincianList) {
                    Integer sksPraktikum = rincian.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum();
                    Integer sksTatapMuka = rincian.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka();

                    int sks = (sksPraktikum != null ? sksPraktikum : 0) + (sksTatapMuka != null ? sksTatapMuka : 0);
                    if (sks == 0) continue;

                    totalSks += sks;
                    BigDecimal angkaMutu = rincian.getAngkaMutu();

                    if (angkaMutu != null) {
                        BigDecimal bobot = angkaMutu.multiply(BigDecimal.valueOf(sks));
                        totalBobot = totalBobot.add(bobot);

                        if (rincian.getHurufMutu() != null &&
                                !rincian.getHurufMutu().equalsIgnoreCase("E")) {
                            sksLulus += sks;
                            totalBobotLulus = totalBobotLulus.add(bobot);
                        }
                    }
                }
            }

            double ipk = totalSks == 0 ? 0 : totalBobot.divide(BigDecimal.valueOf(totalSks), 2, RoundingMode.HALF_UP).doubleValue();
            double ipkLulus = sksLulus == 0 ? 0 : totalBobotLulus.divide(BigDecimal.valueOf(sksLulus), 2, RoundingMode.HALF_UP).doubleValue();

            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setNim(mahasiswa.getNpm());
            profileInfo.setNamaMahasiswa(mahasiswa.getNama());
            profileInfo.setProgramStudi(mahasiswa.getSiakProgramStudi().getNamaProgramStudi());
            profileInfo.setStatusMahasiwa(mahasiswa.getStatusMahasiswa());
            profileInfo.setAngkatan(mahasiswa.getAngkatan());
            profileInfo.setTahunKurikulum(mahasiswa.getKurikulum());
            profileInfo.setSemester(mahasiswa.getSemester());
            profileInfo.setPembimbingAkademik(
                    pembimbingAkademik != null ? pembimbingAkademik.getSiakDosen().getNama() : "-"
            );

            profileInfo.setTotalSks(totalSks);
            profileInfo.setSksLulus(sksLulus);
            profileInfo.setIpkLulus(ipkLulus);
            profileInfo.setIpk(ipk);

            return profileInfo;
        }

        private PerkuliahanChartDto buildPerkuliahanChart(List<Object[]> sksDiambilData) {
            List<SksPerSemesterDto> sksList = sksDiambilData.stream()
                    .map(row -> new SksPerSemesterDto((Integer) row[0], ((Number) row[1]).intValue()))
                    .collect(Collectors.toList());

            return PerkuliahanChartDto.builder()
                    .sksDiambilPerSemester(sksList)
                    .zonaPeringatan(new ZonaStudiDto(9))
                    .zonaDropOut(new ZonaStudiDto(15))
                    .build();
        }

        private ProgresSksChartDto buildProgresSksChart(List<HasilStudi> riwayatHasilStudi) {
            List<SksLulusKumulatifDto> sksLulusList = riwayatHasilStudi.stream()
                    .map(hs -> new SksLulusKumulatifDto(hs.getSemester(), hs.getSksLulus()))
                    .collect(Collectors.toList());

            return ProgresSksChartDto.builder()
                    .batasLulus(SKS_BATAS_LULUS)
                    .sksLulusKumulatif(sksLulusList)
                    .build();
        }

        private IndeksPrestasiChartDto buildIndeksPrestasiChart(List<HasilStudi> riwayatHasilStudi) {
            List<RiwayatIpDto> riwayatIpList = riwayatHasilStudi.stream()
                    .map(hs -> new RiwayatIpDto(hs.getSemester(), hs.getIps(), hs.getIpk()))
                    .collect(Collectors.toList());

            return IndeksPrestasiChartDto.builder()
                    .ipMinimum(IP_MINIMUM)
                    .riwayat(riwayatIpList)
                    .build();
        }

        private SksTempuhChartDto buildSksTempuhChart(List<HasilStudi> riwayatHasilStudi, List<Object[]> sksDiambilData) {
            int sksLulus = riwayatHasilStudi.isEmpty() ? 0 : riwayatHasilStudi.get(riwayatHasilStudi.size() - 1).getSksLulus();
            int sisaSksUntukLulus = SKS_BATAS_LULUS - sksLulus;

            return SksTempuhChartDto.builder()
                    .lulus(sksLulus)
                    .belumLulus(sisaSksUntukLulus - sksLulus)
                    .total(SKS_BATAS_LULUS)
                    .build();
        }

        private DistribusiNilaiChartDto buildDistribusiNilaiChart(List<Object[]> distribusiNilaiData) {
            Map<String, Integer> sksByGrade = distribusiNilaiData.stream()
                    .collect(Collectors.toMap(
                            row -> (String) row[0],
                            row -> ((Number) row[1]).intValue()
                    ));

            List<DetailNilaiDto> detailList = Stream.of("A", "AB", "B", "BC", "C", "D", "E")
                    .map(grade -> new DetailNilaiDto(grade, sksByGrade.getOrDefault(grade, 0)))
                    .collect(Collectors.toList());

            return DistribusiNilaiChartDto.builder().detail(detailList).build();
        }
    }
