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
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanMahasiswa(UUID mahasiswaId, String namaPeriode) {
        List<String> daysOfWeek = Arrays.asList("senin", "selasa", "rabu", "kamis", "jumat", "sabtu");

        Map<String, List<JadwalMingguanResDto>> jadwalByDay = new LinkedHashMap<>();
        for (String day : daysOfWeek) {
            jadwalByDay.put(day, new ArrayList<>());
        }

        Set<UUID> kelasKuliahIds = getEnrolledKelasKuliahIds(mahasiswaId, namaPeriode);
        if (kelasKuliahIds.isEmpty()) {
            return jadwalByDay;
        }

        List<JadwalKuliah> semuaJadwalRelevant = jadwalKuliahRepository
                .findBySiakKelasKuliahIdInAndIsDeletedFalseFetchingRelations(new ArrayList<>(kelasKuliahIds));

        for (JadwalKuliah jadwal : semuaJadwalRelevant) {
            JadwalMingguanResDto itemDto = mapToJadwalDto(jadwal);
            String dayKey = jadwal.getHari().toLowerCase();

            if (jadwalByDay.containsKey(dayKey)) {
                jadwalByDay.get(dayKey).add(itemDto);
            }
        }

        jadwalByDay.forEach((day, items) -> items.sort(Comparator.comparing(JadwalMingguanResDto::getJamMulai)));

        return jadwalByDay;
    }

    @Override
    public List<JadwalMingguanResDto> getJadwalHarianMahasiswa(UUID mahasiswaId, String namaPeriode, String hari) {
        Set<UUID> kelasKuliahIds = getEnrolledKelasKuliahIds(mahasiswaId, namaPeriode);
        if (kelasKuliahIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<JadwalKuliah> jadwalHarianList = jadwalKuliahRepository
                .findByKelasKuliahIdsAndHari(new ArrayList<>(kelasKuliahIds), hari);

        return jadwalHarianList.stream()
                .map(this::mapToJadwalDto)
                .sorted(Comparator.comparing(JadwalMingguanResDto::getJamMulai))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<JadwalMingguanResDto>> getJadwalMingguanDosen(UUID dosenId, String namaPeriode){
        List<String> daysOfWeek = Arrays.asList("senin", "selasa", "rabu", "kamis", "jumat", "sabtu");

        Map<String, List<JadwalMingguanResDto>> jadwalByDay = new LinkedHashMap<>();
        for (String day : daysOfWeek) {
            jadwalByDay.put(day, new ArrayList<>());
        }

        List<JadwalKuliah> semuaJadwalRelevant = jadwalKuliahRepository
                .getjadwalKuliahDosenMingguan(dosenId, namaPeriode);

        for (JadwalKuliah jadwal : semuaJadwalRelevant) {
            JadwalMingguanResDto itemDto = mapToJadwalDto(jadwal);
            String dayKey = jadwal.getHari().toLowerCase();

            if (jadwalByDay.containsKey(dayKey)) {
                jadwalByDay.get(dayKey).add(itemDto);
            }
        }

        jadwalByDay.forEach((day, items) -> items.sort(Comparator.comparing(JadwalMingguanResDto::getJamMulai)));

        return jadwalByDay;
    }

    @Override
    public List<JadwalMingguanResDto> getJadwalHarianDosen(UUID dosenId, String namaPeriode, String hari) {
        List<JadwalKuliah> jadwalHarianList = jadwalKuliahRepository
                .getjadwalKuliahDosenHarian(dosenId, namaPeriode, hari);

        return jadwalHarianList.stream()
                .map(this::mapToJadwalDto)
                .sorted(Comparator.comparing(JadwalMingguanResDto::getJamMulai))
                .collect(Collectors.toList());
    }


    private Set<UUID> getEnrolledKelasKuliahIds(UUID mahasiswaId, String namaPeriode) {
        List<KrsRincianMahasiswa> rincianKrsList = krsRincianMahasiswaRepository
                .findBySiakKrsMahasiswaSiakMahasiswaIdAndSiakKrsMahasiswaSiakPeriodeAkademikIdAndIsDeletedFalse(
                        mahasiswaId, namaPeriode);

        return rincianKrsList.stream()
                .filter(rincian -> rincian.getSiakKelasKuliah() != null)
                .map(rincian -> rincian.getSiakKelasKuliah().getId())
                .collect(Collectors.toSet());
    }

    // Helper method to map from entity to DTO, avoiding code duplication
    private JadwalMingguanResDto mapToJadwalDto(JadwalKuliah jadwal) {
        KelasKuliah kelasKuliah = jadwal.getSiakKelasKuliah();
        MataKuliah mataKuliah = kelasKuliah.getSiakMataKuliah();
        Ruangan ruangan = jadwal.getSiakRuangan();
        Dosen dosen = jadwal.getSiakDosen();

        // Assumption: Dosen entity has getNama() and KelasKuliah has getNama()
        return new JadwalMingguanResDto(
                mataKuliah.getNamaMataKuliah(),
                mataKuliah.getKodeMataKuliah(),
                jadwal.getJamMulai() != null ? jadwal.getJamMulai().format(TIME_FORMATTER) : "N/A",
                jadwal.getJamSelesai() != null ? jadwal.getJamSelesai().format(TIME_FORMATTER) : "N/A",
                kelasKuliah.getNama(),
                ruangan.getNamaRuangan(),
                dosen != null ? dosen.getNama() : "N/A"
        );
    }
}