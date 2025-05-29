package com.siakad.service.impl;

import com.siakad.dto.request.CapaianPembelajaranLulusanReqDto;
import com.siakad.dto.response.CapaianPembelajaranLulusanResDto;
import com.siakad.dto.transform.CapaianPembelajaranLulusanTransform;
import com.siakad.entity.CapaianPembelajaranLulusan;
import com.siakad.entity.ProfilLulusan;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.TahunKurikulum;
import com.siakad.entity.service.CapaianPembelajaranLulusanSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.CapaianPembelajaranLulusanRepository;
import com.siakad.repository.ProfilLulusanRepository;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.repository.TahunKurikulumRepository;
import com.siakad.service.CapaianPembelajaranLulusanService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CapaianPembelajaranLulusanServiceImpl implements CapaianPembelajaranLulusanService {

    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final ProfilLulusanRepository profilLulusanRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final CapaianPembelajaranLulusanRepository capaianPembelajaranLulusanRepository;
    private final UserActivityService service;
    private final CapaianPembelajaranLulusanTransform mapper;

    @Override
    public CapaianPembelajaranLulusanResDto save(CapaianPembelajaranLulusanReqDto reqDto, HttpServletRequest servletRequest) {
        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        CapaianPembelajaranLulusan entity = mapper.toEntity(reqDto);
        entity.setSiakProgramStudi(programStudi);
        entity.setSiakTahunKurikulum(tahunKurikulum);
        entity.setIsDeleted(false);
        if(reqDto.getProfilLulusanIds() != null && !reqDto.getProfilLulusanIds().isEmpty()) {
            List<ProfilLulusan> profilLulusanList = profilLulusanRepository.findAllByIdInAndIsDeletedFalse(reqDto.getProfilLulusanIds());
            entity.setProfilLulusanList(profilLulusanList);
        }
        CapaianPembelajaranLulusan saved = capaianPembelajaranLulusanRepository.save(entity);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_CAPAIAN_PEMBELAJARAN_LULUSAN);

        return mapper.toDto(saved);
    }

    @Override
    public CapaianPembelajaranLulusanResDto getOne(UUID id) {
        CapaianPembelajaranLulusan capaianPembelajaranLulusan = capaianPembelajaranLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Pembelajaran tidak ditemukan : " + id));
        return mapper.toDto(capaianPembelajaranLulusan);
    }

    @Override
    public Page<CapaianPembelajaranLulusanResDto> getPaginate(String tahunKurukulum, Pageable pageable) {
        CapaianPembelajaranLulusanSpecification specBuilder = new CapaianPembelajaranLulusanSpecification();
        Specification<CapaianPembelajaranLulusan> spec = specBuilder.entitySearch(tahunKurukulum);
        Page<CapaianPembelajaranLulusan> all = capaianPembelajaranLulusanRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public CapaianPembelajaranLulusanResDto update(UUID id, CapaianPembelajaranLulusanReqDto reqDto, HttpServletRequest servletRequest) {
        CapaianPembelajaranLulusan capaianPembelajaranLulusan = capaianPembelajaranLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Pembelajaran tidak ditemukan : " + id));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        mapper.toEntity(reqDto, capaianPembelajaranLulusan);
        capaianPembelajaranLulusan.setSiakProgramStudi(programStudi);
        capaianPembelajaranLulusan.setSiakTahunKurikulum(tahunKurikulum);
        if(reqDto.getProfilLulusanIds() != null && !reqDto.getProfilLulusanIds().isEmpty()) {
            List<ProfilLulusan> profilLulusanList = profilLulusanRepository.findAllByIdInAndIsDeletedFalse(reqDto.getProfilLulusanIds());
            capaianPembelajaranLulusan.setProfilLulusanList(profilLulusanList);
        }
        capaianPembelajaranLulusan.setUpdatedAt(LocalDateTime.now());

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_CAPAIAN_PEMBELAJARAN_LULUSAN);
        return mapper.toDto(capaianPembelajaranLulusan);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        CapaianPembelajaranLulusan capaianPembelajaranLulusan = capaianPembelajaranLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Pembelajaran tidak ditemukan : " + id));

        capaianPembelajaranLulusan.setIsDeleted(true);
        CapaianPembelajaranLulusan saved = capaianPembelajaranLulusanRepository.save(capaianPembelajaranLulusan);
        service.saveUserActivity(servletRequest,  MessageKey.DELETE_CAPAIAN_PEMBELAJARAN_LULUSAN);
        mapper.toDto(saved);

    }
}
