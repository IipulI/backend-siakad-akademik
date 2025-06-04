package com.siakad.service.impl;

import com.siakad.dto.response.HasilStudiDto;
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

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HasilStudiServiceImpl implements HasilStudiService {

    private final UserActivityService userActivityService;
    private final HasilStudiRepository hasilStudiRepository;
    private final KrsRincianMahasiswaRepository krsRincianMahasiswaRepository;
    private final HasilStudiTransform mapper;

    @Override
    public HasilStudiDto getHasilStudi(UUID periodeAkademikId) {
        User user = userActivityService.getCurrentUser();
        HasilStudi hasilStudi = hasilStudiRepository
                .findBySiakMahasiswa_IdAndSiakPeriodeAkademik_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId(), periodeAkademikId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Hasil studi not found"));

        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_SiakMahasiswa_IdAndSiakKrsMahasiswa_SiakPeriodeAkademik_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId(), periodeAkademikId);

        return mapper.hasilStudiToDtoWithRincian(hasilStudi, rincianList);
    }

    @Override
    public TranskipDto getTranskip() {
        User user = userActivityService.getCurrentUser();
        HasilStudi hasilStudi = hasilStudiRepository
                .findBySiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Hasil studi not found"));


        List<KrsRincianMahasiswa> rincianList = krsRincianMahasiswaRepository
                .findAllBySiakKrsMahasiswa_SiakMahasiswa_IdAndIsDeletedFalse(user.getSiakMahasiswa().getId());

        return mapper.hasilStudiToDtoTranskip(hasilStudi, rincianList);
    }
}