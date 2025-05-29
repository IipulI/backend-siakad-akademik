package com.siakad.dto.transform.helper;

import com.siakad.dto.response.KrsResDto;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.repository.JadwalKuliahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JadwalKuliahMapperHelper {

    @Autowired
    private JadwalKuliahRepository jadwalKuliahRepository;

    public void mapJadwalKuliahToDto(KrsResDto dto, KrsRincianMahasiswa entity) {
        List<JadwalKuliah> jadwalList = jadwalKuliahRepository
                .findBySiakKelasKuliahId(entity.getSiakKelasKuliah().getId());

        if (!jadwalList.isEmpty()) {
            JadwalKuliah jadwal = jadwalList.get(0);
            dto.setHari(jadwal.getHari());
            dto.setJamMulai(jadwal.getJamMulai().toString());
            dto.setJamSelesai(jadwal.getJamSelesai().toString());
            dto.setDosenPengajar(jadwal.getSiakDosen().getNama());
        }
    }
}
