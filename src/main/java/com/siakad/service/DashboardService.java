package com.siakad.service;

import com.siakad.dto.response.*;

import java.util.List;
import java.util.UUID;

public interface DashboardService {
    // -- Akademik

    CardDto getCard();
    List<AkmAngkatanDto> getAkmAngkatan();
    List<AkmProdiDto> getJumlahMahasiswaPerProdi();
    List<MahasiswaBaruDto> getMahasiswaBaru();
    TagihanKomponenMahasiswaDto getTagihanKomponenMahasiswa(UUID id);

    // -- Mahasiswa
    TagihanMahasiswaDto getTagihanMahasiswa();
    List<TagihanMhsDto> getTagihanMhs(UUID mahasiswaId, String status, String namaPeriode, String keyword);
    InfoTagihanResDto getTagihanInfo(UUID mahasiswaId);

    GrafikAkademikDto getGrafikAkademik();
    TagihanKomponenMahasiswaDto getTagihanKomponenMahasiswa();
    List<RiwayatTagihanDto> getRiwayatTagihan();
    DetailRiwayatTagihanDto getDetailRiwayatTagihan(UUID id);
}
