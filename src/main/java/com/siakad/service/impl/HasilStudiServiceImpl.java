package com.siakad.service.impl;

import com.siakad.dto.response.HasilStudiDto;
import com.siakad.dto.response.RincianKrsDto;
import com.siakad.dto.response.TranskipDto;
import com.siakad.dto.transform.HasilStudiTransform;
import com.siakad.entity.HasilStudi;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.HasilStudiRepository;
import com.siakad.repository.KrsRincianMahasiswaRepository;
import com.siakad.service.HasilStudiService;
import com.siakad.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HasilStudiServiceImpl implements HasilStudiService {

    private final UserActivityService userActivityService;
    private final HasilStudiRepository hasilStudiRepository;
    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final HasilStudiTransform mapper;

    @Override
    public HasilStudiDto getHasilStudi(String namaPeriode) {
        User user = userActivityService.getCurrentUser();
        HasilStudi hasilStudi = hasilStudiRepository
                .findBySiakMahasiswaAndByNamaPeriode(user.getSiakMahasiswa().getId(), namaPeriode)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Hasil studi not found"));


        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findByMahasiswaAndByNamaPeriode(user.getSiakMahasiswa().getId(), namaPeriode);

        return mapper.hasilStudiToDtoWithRincian(hasilStudi, rincianList);
    }

    @Override
    public HasilStudiDto getMkMengulang(UUID periodeAkademikId) {
        User user = userActivityService.getCurrentUser();
        HasilStudi hasilStudi = hasilStudiRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId(), periodeAkademikId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Hasil studi not found"));

        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_SiakPeriodeAkademik_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId(), periodeAkademikId);

        return mapper.hasilStudiToDtoWithRincian(hasilStudi, rincianList);
    }


    @Override
    public TranskipDto buildTranskip(List<KrsRincianMahasiswa> rincianList) {
        Map<UUID, List<KrsRincianMahasiswa>> grouped = rincianList.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getSiakKelasKuliah().getId()
                ));

        List<RincianKrsDto> rincianKrsDtoList = new ArrayList<>();

        for (Map.Entry<UUID, List<KrsRincianMahasiswa>> entry : grouped.entrySet()) {
            List<KrsRincianMahasiswa> group = entry.getValue();

            List<KrsRincianMahasiswa> lulusList = group.stream()
                    .filter(k -> "Lulus".equalsIgnoreCase(k.getStatus() != null ? k.getStatus().trim() : ""))
                    .collect(Collectors.toList());

            List<KrsRincianMahasiswa> selectedList = new ArrayList<>();

            if (!lulusList.isEmpty()) {
                selectedList.addAll(lulusList); // ambil semua yang lulus
            } else {
                group.sort(Comparator.comparing(KrsRincianMahasiswa::getUpdatedAt));
                selectedList.add(group.get(group.size() - 1)); // ambil yang terbaru
            }

            for (KrsRincianMahasiswa selected : selectedList) {
                int semester = selected.getSiakKrsMahasiswa().getSemester();
                var mataKuliah = selected.getSiakKelasKuliah().getSiakMataKuliah();
                int sks = mataKuliah.getSksPraktikum() + mataKuliah.getSksTatapMuka();
                BigDecimal angkaMutu = selected.getAngkaMutu() != null ? selected.getAngkaMutu() : BigDecimal.ZERO;
                BigDecimal jumlahAngkaMutu = angkaMutu.multiply(BigDecimal.valueOf(sks));

                RincianKrsDto dto = new RincianKrsDto();
                dto.setNamaMataKuliah(mataKuliah.getNamaMataKuliah());
                dto.setKodeMataKuliah(mataKuliah.getKodeMataKuliah());
                dto.setSks(sks);
                dto.setHurufMutu(selected.getHurufMutu());
                dto.setAngkaMutu(angkaMutu);
                dto.setSemester(semester);
                dto.setJumlahAngkaMutu(jumlahAngkaMutu);

                rincianKrsDtoList.add(dto);
            }
        }

        int totalSks = rincianKrsDtoList.stream()
                .mapToInt(RincianKrsDto::getSks)
                .sum();

        BigDecimal totalAngkaMutu = rincianKrsDtoList.stream()
                .map(RincianKrsDto::getJumlahAngkaMutu)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ipk = totalSks == 0
                ? BigDecimal.ZERO
                : totalAngkaMutu.divide(BigDecimal.valueOf(totalSks), 2, RoundingMode.HALF_UP);

        TranskipDto transkipDto = new TranskipDto();
        transkipDto.setRincianKrsDto(rincianKrsDtoList);
        transkipDto.setTotalSks(totalSks);
        transkipDto.setTotalAngkaMutu(totalAngkaMutu);
        transkipDto.setIpk(ipk);

        return transkipDto;
    }


    public List<KrsRincianMahasiswa> getRincianMahasiswa() {
        User currentUser = userActivityService.getCurrentUser();

        UUID mahasiswaId = currentUser.getSiakMahasiswa().getId();

        return krsRincianMahasiswaRepository
                .findAllActiveByMahasiswaId(mahasiswaId);
    }

    @Override
    public List<KrsRincianMahasiswa> getRincianMahasiswa(UUID mahasiswaId) {
        return krsRincianMahasiswaRepository.findAllActiveByMahasiswaId(mahasiswaId);
    }

    @Override
    public HasilStudiDto getHasilStudi(UUID mahasiswaId, UUID periodeAkademikId) {
        HasilStudi hasilStudi = hasilStudiRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, periodeAkademikId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Hasil studi not found"));

        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_SiakPeriodeAkademik_IdAndIsDeletedFalse(mahasiswaId, periodeAkademikId);

        return mapper.hasilStudiToDtoWithRincian(hasilStudi, rincianList);
    }
}