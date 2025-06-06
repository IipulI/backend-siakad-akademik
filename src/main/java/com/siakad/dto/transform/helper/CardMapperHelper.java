package com.siakad.dto.transform.helper;


import com.siakad.entity.Mahasiswa;
import com.siakad.entity.PeriodeAkademik;
import com.siakad.repository.MahasiswaRepository;
import com.siakad.repository.PeriodeAkademikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.swing.plaf.PanelUI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardMapperHelper {

    private final MahasiswaRepository mahasiswaRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;

    public Integer getMahasiswaBaruPeriodeIni() {
        return mahasiswaRepository.findMahasiswaBaruDiPeriodeAkademikAktif();
    }

    public Integer getMahasiswaAktif() {
        return mahasiswaRepository.countMahasiswaAktif();
    }

    public Integer getMahasiswaTerdaftar() {
        return mahasiswaRepository.countAllMahasiswa();
    }

    public String getPeriodeAkademikAktif() {
        return periodeAkademikRepository.findFirstByStatusActive()
                .map(PeriodeAkademik::getNamaPeriode)
                .orElse("Tidak ada periode aktif");
    }
}
