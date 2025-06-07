package com.siakad.service.impl;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import com.siakad.dto.transform.PembimbingAkademikTransform;
import com.siakad.dto.transform.helper.PembimbingAkademikHelper;
import com.siakad.entity.*;
import com.siakad.entity.service.PembimbingAkademikSpecification;
import com.siakad.enums.MessageKey;
import com.siakad.repository.*;
import com.siakad.service.PembimbingAkademikService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final PembimbingAkademikHelper helper;

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

    @Override
    public List<PembimbingAkademikResDto> getAll() {
        return List.of((PembimbingAkademikResDto) null);
    }

    @Override
    public Page<PembimbingAkademikResDto> getAllByPaginate(String periodeAkademik, String programStudi, Integer semester, String angkatan, Pageable pageable) {
        PembimbingAkademikSpecification specBuilder = new PembimbingAkademikSpecification();
        Specification<PembimbingAkademik> spec = specBuilder.entitySearch(periodeAkademik, programStudi, semester, angkatan);

        Page<PembimbingAkademik> entities = pembimbingAkademikRepository.findAll(spec, pageable);

        List<PembimbingAkademikResDto> dtoList = entities.stream().map(entity -> {
            UUID mahasiswaId = entity.getSiakMahasiswa().getId();
            UUID periodeAkademikId = entity.getSiakPeriodeAkademik().getId();

            Integer totalSks = helper.getTotalSks(mahasiswaId);
            Integer batasSks = helper.getBatasSks(mahasiswaId);

            BigDecimal ipk = helper.getIpkByPeriode(mahasiswaId, periodeAkademikId);
            BigDecimal ips = helper.getIpsByPeriode(mahasiswaId, periodeAkademikId);
            boolean diajukan = helper.getStatusDiajukan(mahasiswaId);
            boolean disetujui = helper.getStatusDisetujui(mahasiswaId);
            String namaPembimbing = entity.getSiakDosen() != null
                    ? entity.getSiakDosen().getNidn() + " - " + entity.getSiakDosen().getNama()
                    : null;

            return mapper.toFullDto(entity, batasSks, totalSks, ips, ipk, diajukan, disetujui, namaPembimbing);
        }).toList();

        return new PageImpl<>(dtoList, pageable, entities.getTotalElements());
    }

}
