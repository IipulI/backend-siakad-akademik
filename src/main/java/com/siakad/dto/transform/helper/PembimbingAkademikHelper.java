package com.siakad.dto.transform.helper;

import com.siakad.entity.*;
import com.siakad.repository.BatasSksRepository;
import com.siakad.repository.HasilStudiRepository;
import com.siakad.repository.KrsMahasiswaRepository;
import com.siakad.repository.MahasiswaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PembimbingAkademikHelper {

    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final HasilStudiRepository hasilStudiRepository;
    private final BatasSksRepository batasSksRepository;
    private final MahasiswaRepository mahasiswaRepository;

    public Integer getTotalSks(UUID mahasiswaId) {
        return krsMahasiswaRepository.getJumlahSksDiambilByMahasiswaId(mahasiswaId);
    }

    public Integer getBatasSks(UUID mahasiswaId) {
        BigDecimal ipsTerakhir = hasilStudiRepository
                .findTopBySiakMahasiswa_IdOrderByCreatedAtDesc(mahasiswaId)
                .map(HasilStudi::getIps)
                .orElse(BigDecimal.ZERO);

        Jenjang jenjang = mahasiswaRepository.findByIdAndIsDeletedFalse(mahasiswaId)
                .map(mahasiswa -> {
                    if (mahasiswa.getSiakProgramStudi() == null || mahasiswa.getSiakProgramStudi().getSiakJenjang() == null) {
                        throw new IllegalStateException("Data jenjang mahasiswa tidak lengkap.");
                    }
                    return mahasiswa.getSiakProgramStudi().getSiakJenjang();
                }).orElseThrow(() -> new IllegalArgumentException("Mahasiswa tidak ditemukan dengan ID: " + mahasiswaId));

        BatasSks batasSks = batasSksRepository
                .findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(
                        jenjang, ipsTerakhir, ipsTerakhir
                ).orElseThrow(() -> new RuntimeException("Batas SKS belum diatur untuk IPS: " + ipsTerakhir));

        return batasSks.getBatasSks();
    }


    public Boolean getStatusDiajukan(UUID mahasiswaId) {
        String status = krsMahasiswaRepository.findLatestStatus(mahasiswaId);
        return status.equalsIgnoreCase("Diajukan") || status.equalsIgnoreCase("Disetujui");
    }

    public Boolean getStatusDisetujui(UUID mahasiswaId) {
        String status = krsMahasiswaRepository.findLatestStatus(mahasiswaId);
        return status.equalsIgnoreCase("Disetujui");
    }

    public BigDecimal getIpsByPeriode(UUID mahasiswaId, UUID periodeAkademikId) {
        return hasilStudiRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, periodeAkademikId)
                .map(HasilStudi::getIps)
                .orElse(BigDecimal.ZERO);
    }

    public BigDecimal getIpkByPeriode(UUID mahasiswaId, UUID periodeAkademikId) {
        return hasilStudiRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, periodeAkademikId)
                .map(HasilStudi::getIpk)
                .orElse(BigDecimal.ZERO);
    }
}
