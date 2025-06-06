package com.siakad.service;

import com.siakad.dto.response.*;

import java.util.List;

public interface DashboardService {
    CardDto getCard();
    List<AkmAngkatanDto> getAkmAngkatan();
    List<AkmProdiDto> getJumlahMahasiswaPerProdi();
    List<MahasiswaBaruDto> getMahasiswaBaru();

    // -- Mahasiswa
    TagihanMahasiswaDto getTagihanMahasiswa();
    GrafikAkademikDto getGrafikAkademik();
    TagihanKomponenMahasiswaDto getTagihanKomponenMahasiswa();

}
