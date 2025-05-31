package com.siakad.service.impl;

import com.siakad.dto.response.JadwalMingguanResDto; // Your DTO
import com.siakad.entity.*;
import com.siakad.repository.JadwalKuliahRepository;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import com.siakad.service.JadwalKuliahService;       // Your service interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JadwalKuliahServiceImpl implements JadwalKuliahService {

    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final JadwalKuliahRepository jadwalKuliahRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Autowired
    public JadwalKuliahServiceImpl(KrsRincianMahasiswaRepository krsRincianMahasiswaRepository,
                                   JadwalKuliahRepository jadwalKuliahRepository) {
        this.krsRincianMahasiswaRepository = krsRincianMahasiswaRepository;
        this.jadwalKuliahRepository = jadwalKuliahRepository;
    }

    @Override
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanMahasiswa(UUID mahasiswaId, UUID periodeAkademikId) {
        // 1. Get all KrsRincianMahasiswa for the student and the specified active period
        List<KrsRincianMahasiswa> rincianKrsList = krsRincianMahasiswaRepository
                .findBySiakKrsMahasiswaSiakMahasiswaIdAndSiakKrsMahasiswaSiakPeriodeAkademikIdAndIsDeletedFalse(
                        mahasiswaId, periodeAkademikId);
        // Note: If the above derived query method is problematic or too long,
        // consider using an @Query in your KrsRincianMahasiswaRepository for clarity and control.

        if (rincianKrsList.isEmpty()) {
            return Collections.emptyMap(); // No schedule if not enrolled in anything for the period
        }

        // Collect all KelasKuliah IDs the student is enrolled in, filtering out nulls for safety
        Set<UUID> kelasKuliahIds = rincianKrsList.stream()
                .filter(rincian -> rincian.getSiakKelasKuliah() != null)
                .map(rincian -> rincian.getSiakKelasKuliah().getId())
                .collect(Collectors.toSet());

        if (kelasKuliahIds.isEmpty()) {
            return Collections.emptyMap(); // No KelasKuliah associated
        }

        // 2. Fetch all relevant JadwalKuliah entries for these KelasKuliah IDs
        // Consider using a @Query with JOIN FETCH in JadwalKuliahRepository for this method
        // to optimize fetching of related entities (KelasKuliah, MataKuliah, Ruangan, Dosen)
        // to prevent N+1 query problems.
        // Example: findBySiakKelasKuliahIdInAndIsDeletedFalseFetchingRelations(...)
        List<JadwalKuliah> semuaJadwalRelevant = jadwalKuliahRepository
                .findBySiakKelasKuliahIdInAndIsDeletedFalse(new ArrayList<>(kelasKuliahIds));


        Map<String, List<JadwalMingguanResDto>> jadwalByDay = new LinkedHashMap<>();
        // Using LinkedHashMap to potentially preserve day order if days are processed in a specific order

        for (JadwalKuliah jadwal : semuaJadwalRelevant) {
            // Defensive checks for potentially null related entities if not using JOIN FETCH
            // or if data integrity allows nulls where they are not expected.
            if (jadwal.getSiakKelasKuliah() == null ||
                    jadwal.getSiakKelasKuliah().getSiakMataKuliah() == null ||
                    jadwal.getSiakRuangan() == null ||
                    jadwal.getSiakDosen() == null) { // Assuming Dosen is mandatory for a schedule item
                // Log this occurrence or handle as appropriate
                continue;
            }

            KelasKuliah kelasKuliah = jadwal.getSiakKelasKuliah();
            MataKuliah mataKuliah = kelasKuliah.getSiakMataKuliah();
            Ruangan ruangan = jadwal.getSiakRuangan();
            Dosen dosen = jadwal.getSiakDosen();

            String dosenName = "N/A";
            // Assuming Dosen entity has a getNama() method. Adjust if it's getNamaDosen(), etc.
            if (dosen.getNama() != null) {
                dosenName = dosen.getNama();
            }

            String kelasNama = "N/A";
            // Assuming KelasKuliah entity has a getNama() method for the class name.
            if (kelasKuliah.getNama() != null) {
                kelasNama = kelasKuliah.getNama();
            }

            String jamMulaiStr = "N/A";
            if (jadwal.getJamMulai() != null) {
                jamMulaiStr = jadwal.getJamMulai().format(TIME_FORMATTER);
            }

            String jamSelesaiStr = "N/A";
            if (jadwal.getJamSelesai() != null) {
                jamSelesaiStr = jadwal.getJamSelesai().format(TIME_FORMATTER);
            }


            JadwalMingguanResDto itemDto = new JadwalMingguanResDto(
                    mataKuliah.getNamaMataKuliah(),
                    mataKuliah.getKodeMataKuliah(),
                    jamMulaiStr,
                    jamSelesaiStr,
                    kelasNama,
                    ruangan.getNamaRuangan(),
                    dosenName
            );

            // Normalize day name to lowercase for the map key
            // Assuming jadwal.getHari() returns strings like "Senin", "Selasa", etc.
            String dayKey = jadwal.getHari().toLowerCase();
            jadwalByDay.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(itemDto);
        }

        // Optional: Sort items within each day by jamMulai
        jadwalByDay.forEach((day, items) -> {
            items.sort(Comparator.comparing(JadwalMingguanResDto::getJamMulai));
        });

        return jadwalByDay;
    }
}