package com.siakad.dto.transform.helper;

import com.siakad.dto.response.MahasiswaDto;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RincianKrsMappeHelper {

    @Autowired
    private KrsRincianMahasiswaRepository repository;

    public void mapRincianMahasiswa(MahasiswaDto dto, UUID kelasId, UUID mahasiswaId) {
        repository.findBySiakKelasKuliah_IdAndSiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(kelasId, mahasiswaId)
                .ifPresent(entity -> {
                    dto.setKehadiran(entity.getKehadiran());
                    dto.setTugas(entity.getTugas());
                    dto.setUts(entity.getUts());
                    dto.setUas(entity.getUas());
                    dto.setNilai(entity.getNilai());
                    dto.setGrade(entity.getHurufMutu());
                });
    }
}
