package com.siakad.service.impl;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import com.siakad.dto.transform.PembimbingAkademikTransform;
import com.siakad.entity.*;
import com.siakad.enums.MessageKey;
import com.siakad.repository.*;
import com.siakad.service.PembimbingAkademikService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PembimbingAkademikServiceImpl implements PembimbingAkademikService {

    private final PembimbingAkademikTransform mapper;
    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final UserActivityServiceImpl service;
    private final PembimbingAkademikRepository pembimbingAkademikRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final DosenRepository dosenRepository;

    @Override
    public List<PembimbingAkademikResDto> save(PembimbingAkademikReqDto reqDto, HttpServletRequest servletRequest) {

        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(reqDto.getPeriodeAkademikId())
                .orElseThrow(() -> new RuntimeException("Periode Akademik tidak ditemukan"));

        Dosen dosen = dosenRepository.findByIdAndIsDeletedFalse(reqDto.getDosenId())
                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));

        List<PembimbingAkademikResDto> result = new ArrayList<>();

        for (UUID mahasiswaId : reqDto.getMahasiswaIds()) {
            Mahasiswa mahasiswa = mahasiswaRepository.findByIdAndIsDeletedFalse(mahasiswaId)
                    .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

            var pembimbingAkademik = mapper.toEntity(reqDto);

            pembimbingAkademik.setSiakMahasiswa(mahasiswa);
            pembimbingAkademik.setSiakPeriodeAkademik(periodeAkademik);
            pembimbingAkademik.setSiakDosen(dosen);
            pembimbingAkademik.setIsDeleted(false);
            pembimbingAkademikRepository.save(pembimbingAkademik);
            result.add(mapper.toDto(pembimbingAkademik));
        }

        service.saveUserActivity(servletRequest, MessageKey.CREATE_PEMBIMBING_AKADEMIK);
        return result;
    }

}
