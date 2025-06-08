package com.siakad.dto.transform.helper;

import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.repository.JadwalKuliahRepository;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KelasKuliahMapperHelper {

    private final JadwalKuliahRepository jadwalKuliahRepository;
    private final KrsRincianMahasiswaRepository rincianRepository;

    public List<String> mapDosen(KelasKuliah kelasKuliah) {
        return jadwalKuliahRepository.findBySiakKelasKuliahId(kelasKuliah.getId())
                .stream()
                .map(JadwalKuliah::getSiakDosen)
                .filter(Objects::nonNull)
                .map(Dosen::getNama)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> mapHariJadwal(KelasKuliah kelasKuliah) {
        return jadwalKuliahRepository.findBySiakKelasKuliahId(kelasKuliah.getId())
                .stream()
                .map(JadwalKuliah::getHari)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public int hitungJumlahPeserta(KelasKuliah kelas) {
        return rincianRepository.countBySiakKelasKuliahAndIsDeletedFalse(kelas);
    }

    public String tentukanStatusPenilaian(KelasKuliah kelas) {
        List<KrsRincianMahasiswa> rincianList = rincianRepository.findBySiakKelasKuliahAndIsDeletedFalse(kelas);
        boolean adaPenilaian = rincianList.stream().anyMatch(r ->
                r.getNilaiAkhir() != null ||
                        r.getNilai() != null ||
                        r.getAngkaMutu() != null ||
                        r.getHurufMutu() != null
        );
        return adaPenilaian ? "Ada" : "Belum Ada";
    }
}