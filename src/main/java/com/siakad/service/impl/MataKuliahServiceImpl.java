package com.siakad.service.impl;

import com.siakad.dto.request.KurikulumProdiReqDto;
import com.siakad.dto.request.MataKuliahReqDto;
import com.siakad.dto.response.KurikulumProdiResDto;
import com.siakad.dto.response.MataKuliahResDto;
import com.siakad.dto.transform.MataKuliahTransform;
import com.siakad.entity.MataKuliah;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.TahunKurikulum;
import com.siakad.entity.service.MataKuliahSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.MataKuliahRepository;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.repository.TahunKurikulumRepository;
import com.siakad.service.MataKuliahService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class MataKuliahServiceImpl implements MataKuliahService {


    private final MataKuliahRepository mataKuliahRepository;
    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final UserActivityService service;
    private final MataKuliahTransform mapper;

    @Override
    public MataKuliahResDto create(MataKuliahReqDto request, HttpServletRequest servletRequest) {
        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(request.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + request.getSiakTahunKurikulumId()));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + request.getSiakProgramStudiId()));

        MataKuliah prasyaratMatakuliah1 = null;
        if (request.getPrasyaratMataKuliah1Id() != null) {
            prasyaratMatakuliah1 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah1Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 1 tidak ditemukan : " + request.getPrasyaratMataKuliah1Id()));
        }

        MataKuliah prasyaratMatakuliah2 = null;
        if (request.getPrasyaratMataKuliah2Id() != null) {
            prasyaratMatakuliah2 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah2Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 2 tidak ditemukan : " + request.getPrasyaratMataKuliah2Id()));
        }


        MataKuliah prasyaratMatakuliah3 = null;
        if (request.getPrasyaratMataKuliah3Id() != null) {
            prasyaratMatakuliah3 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah3Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 1 tidak ditemukan : " + request.getPrasyaratMataKuliah3Id()));
        }

        MataKuliah mataKuliah = mapper.toEntity(request);
        mataKuliah.setSiakTahunKurikulum(tahunKurikulum);
        mataKuliah.setSiakProgramStudi(programStudi);
        mataKuliah.setPrasyaratMataKuliah1(prasyaratMatakuliah1);
        mataKuliah.setPrasyaratMataKuliah2(prasyaratMatakuliah2);
        mataKuliah.setPrasyaratMataKuliah3(prasyaratMatakuliah3);
        mataKuliah.setIsDeleted(false);
        mataKuliahRepository.save(mataKuliah);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_MATA_KULIAH);
        return mapper.toDto(mataKuliah);
    }

    @Override
    public Page<MataKuliahResDto> search(String keyword, String programStudi, String jenisMataKuliah, String tahunKurikulum, Pageable pageable) {
        MataKuliahSpecification specBuilder = new MataKuliahSpecification();
        Specification<MataKuliah> spec = specBuilder.entitySearch(keyword, programStudi, jenisMataKuliah, tahunKurikulum, null);
        Page<MataKuliah> all = mataKuliahRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public MataKuliahResDto getOne(UUID id) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + id));

        return mapper.toDto(mataKuliah);
    }

    @Override
    public MataKuliahResDto update(MataKuliahReqDto request, UUID id, HttpServletRequest servletRequest) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + id));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(request.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + request.getSiakTahunKurikulumId()));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + request.getSiakProgramStudiId()));

        MataKuliah prasyaratMatakuliah1 = null;
        if (request.getPrasyaratMataKuliah1Id() != null) {
            prasyaratMatakuliah1 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah1Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 1 tidak ditemukan : " + request.getPrasyaratMataKuliah1Id()));
        }

        MataKuliah prasyaratMatakuliah2 = null;
        if (request.getPrasyaratMataKuliah2Id() != null) {
            prasyaratMatakuliah2 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah2Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 2 tidak ditemukan : " + request.getPrasyaratMataKuliah2Id()));
        }


        MataKuliah prasyaratMatakuliah3 = null;
        if (request.getPrasyaratMataKuliah3Id() != null) {
            prasyaratMatakuliah3 = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getPrasyaratMataKuliah3Id())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                            "Prasyarat Mata Kuliah 3 tidak ditemukan : " + request.getPrasyaratMataKuliah3Id()));
        }

        mapper.toEntity(request, mataKuliah);
        mataKuliah.setSiakTahunKurikulum(tahunKurikulum);
        mataKuliah.setSiakProgramStudi(programStudi);
        mataKuliah.setPrasyaratMataKuliah1(prasyaratMatakuliah1);
        mataKuliah.setPrasyaratMataKuliah2(prasyaratMatakuliah2);
        mataKuliah.setPrasyaratMataKuliah3(prasyaratMatakuliah3);
        mataKuliah.setUpdatedAt(LocalDateTime.now());
        mataKuliahRepository.save(mataKuliah);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_MATA_KULIAH);
        return mapper.toDto(mataKuliah);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + id));

        mataKuliah.setIsDeleted(true);
        MataKuliah saved = mataKuliahRepository.save(mataKuliah);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_MATA_KULIAH);
        mapper.toDto(saved);
    }

    @Override
    public void updateKurikulum(UUID id, KurikulumProdiReqDto request, HttpServletRequest servletRequest) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + id));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(request.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + request.getSiakTahunKurikulumId()));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + request.getSiakProgramStudiId()));

        mapper.toEntity(request, mataKuliah);
        mataKuliah.setSiakTahunKurikulum(tahunKurikulum);
        mataKuliah.setSiakProgramStudi(programStudi);
        mataKuliah.setSemester(request.getSemester());
        mataKuliah.setNilaiMin(request.getNilaiMin());
        mataKuliah.setOpsiMataKuliah(request.getOpsiMataKuliah());
        mataKuliah.setUpdatedAt(LocalDateTime.now());
        mataKuliahRepository.save(mataKuliah);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_MATA_KULIAH);
        mapper.toDto(mataKuliah);
    }

    @Override
    public List<KurikulumProdiResDto> getKurikulumPerSemester(String programStudi, String tahunKurikulum) {

        MataKuliahSpecification specBuilder = new MataKuliahSpecification();
        Specification<MataKuliah> spec = specBuilder.entitySearchKurikulum(programStudi, tahunKurikulum);

        List<MataKuliah> mataKuliahList = mataKuliahRepository.findAll(spec);

        List<MataKuliahResDto> mataKuliahResDtos = mapper.toDtoList(mataKuliahList);

        Map<String, List<MataKuliahResDto>> grouped = mataKuliahResDtos.stream()
                .filter(mk -> mk.getSemester() != null)
                .collect(Collectors.groupingBy(MataKuliahResDto::getSemester));

        return grouped.entrySet().stream().map(entry -> {
            KurikulumProdiResDto dto = new KurikulumProdiResDto();
            dto.setSemester(entry.getKey());
            dto.setMataKuliah(entry.getValue());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<MataKuliahResDto> getPaginated(String keyword, UUID dosenId, Pageable pageable){
        MataKuliahSpecification specBuilder = new MataKuliahSpecification();

        List<MataKuliah> mataKuliahRelatedToDosen = mataKuliahRepository.findRelatedtoDosen(dosenId);

        Collection<UUID> mataKuliahUuids = mataKuliahRelatedToDosen.stream()
                .map(MataKuliah::getId)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        Specification<MataKuliah> spec = specBuilder.entitySearch(keyword, null, null, null, mataKuliahUuids);
        Page<MataKuliah> all = mataKuliahRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }
}
