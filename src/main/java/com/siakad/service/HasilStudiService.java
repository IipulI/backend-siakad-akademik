package com.siakad.service;

import com.siakad.dto.request.HasilStudiReqDto;
import com.siakad.dto.response.HasilStudiDto;
import com.siakad.dto.response.TranskipDto;
import com.siakad.entity.HasilStudi;
import com.siakad.entity.KrsRincianMahasiswa;


import java.util.List;
import java.util.UUID;

public interface HasilStudiService {
    HasilStudiDto getHasilStudi(UUID periodeAkademikId);
    HasilStudiDto getMkMengulang(UUID periodeAkademikId);
    TranskipDto buildTranskip(List<KrsRincianMahasiswa> rincianList);

    // Mahasiswa
    List<KrsRincianMahasiswa> getRincianMahasiswa();

    // Akademik
    List<KrsRincianMahasiswa> getRincianMahasiswa(UUID mahasiswaId);
    HasilStudiDto getHasilStudi(UUID mahasiswaId, UUID periodeAkademikId);

}
