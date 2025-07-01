package com.siakad.service.impl;

import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.RpsDetailResDto;
import com.siakad.dto.response.RpsMataKuliahDto;
import com.siakad.dto.response.RpsResDto;
import com.siakad.dto.transform.RpsTransform;
import com.siakad.dto.transform.helper.RpsRawDataToDtoMapper;
import com.siakad.entity.*;
import com.siakad.entity.service.RpsSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.RpsService;
import com.siakad.service.UserActivityService;
import com.siakad.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RpsServiceImpl implements RpsService {

    private final RpsRepository rpsRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final DosenRepository dosenRepository;
    private final KelasKuliahRepository kelasKuliahRepository;
    private final UserActivityService service;
    private final RpsTransform mapper;
    private final KelasRpsRepository kelasRpsRepository;
    private final UserActivityService userActivityService;
    private final RpsRawDataToDtoMapper rpsRawDataToDtoMapper;

    @Override
    public void create(RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest request) throws IOException {
        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(reqDto.getSiakMataKuliahId()).
                orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + reqDto.getSiakMataKuliahId()));

        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(reqDto.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik tidak ditemukan : " + reqDto.getSiakPeriodeAkademikId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        Rps entity = mapper.toEntity(reqDto);
        entity.setDokumenRps(FileUtils.compress(dokumenRps.getBytes()));
        entity.setSiakMataKuliah(mataKuliah);
        entity.setSiakProgramStudi(programStudi);
        entity.setSiakPeriodeAkademik(periodeAkademik);
        entity.setSiakTahunKurikulum(tahunKurikulum);
        entity.setIsDeleted(false);

        if(reqDto.getDosenIds() != null && !reqDto.getDosenIds().isEmpty()) {
            List<Dosen> dosenList = dosenRepository.findAllByIdAndIsDeletedFalse(reqDto.getDosenIds());
            entity.setDosenList(dosenList);
        }

        rpsRepository.save(entity);
        service.saveUserActivity(request, MessageKey.CREATE_RPS);
    }

    @Override
    public RpsResDto getOne(UUID id) {
        Rps rps = rpsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Rps tidak ditemukan : " + id));

        return mapper.toDto(rps);
    }

    @Override
    public List<RpsMataKuliahDto> getRpsByMataKuliah(UUID mataKuliahId) {
        List<Rps> rpsList = rpsRepository.findAllBySiakMataKuliah_IdAndIsDeletedFalse(mataKuliahId);

        return rpsList.stream().map(rps -> {
            RpsMataKuliahDto dto = new RpsMataKuliahDto();
            dto.setId(rps.getId());
            dto.setPeriodeAkademik(mapper.periodeAkademikToDto(rps.getSiakPeriodeAkademik()));
            dto.setDosenPenyusun(mapper.dosenListToDtoList(rps.getDosenList()));
            dto.setKelas(mapper.kelasKuliahListToDtoList(rps.getKelasKuliahList()));
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public RpsResDto update(UUID id, RpsReqDto reqDto, MultipartFile dokumenRps, HttpServletRequest servletRequest) throws IOException {

        Rps entity = rpsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Rps tidak ditemukan : " + id));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(reqDto.getSiakMataKuliahId()).
                orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + reqDto.getSiakMataKuliahId()));

        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(reqDto.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik tidak ditemukan : " + reqDto.getSiakPeriodeAkademikId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        mapper.toEntity(reqDto, entity);
        entity.setId(id);
        entity.setDokumenRps(FileUtils.compress(dokumenRps.getBytes()));
        entity.setSiakMataKuliah(mataKuliah);
        entity.setSiakProgramStudi(programStudi);
        entity.setSiakPeriodeAkademik(periodeAkademik);
        entity.setSiakTahunKurikulum(tahunKurikulum);
        entity.setUpdatedAt(LocalDateTime.now());
        if(reqDto.getDosenIds() != null && !reqDto.getDosenIds().isEmpty()) {
            List<Dosen> dosenList = dosenRepository.findAllByIdAndIsDeletedFalse(reqDto.getDosenIds());
            entity.setDosenList(dosenList);
        }

        Rps saved = rpsRepository.save(entity);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_RPS);
        return mapper.toDto(saved);
    }

    @Override
    public byte[] getDokumenRps(UUID id) {
        Optional<Rps> rps = rpsRepository.findByIdAndIsDeletedFalse(id);
        return FileUtils.decompress(rps.get().getDokumenRps());
    }

    @Override
    public Page<RpsResDto> getPaginate(String tahunKurikulum, String programStudi, String periodeAkademik, Boolean hasKelas, Pageable pageable) {
        RpsSpecification specBuilder = new RpsSpecification();
        Specification<Rps> spec = specBuilder.entitySearch(tahunKurikulum, programStudi, periodeAkademik, hasKelas);
        Page<Rps> all = rpsRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        Rps entity = rpsRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Rps tidak ditemukan : " + id));

        entity.setIsDeleted(true);
        Rps saved = rpsRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_RPS);
        mapper.toDto(saved);
    }

    @Override
    public void createKelas(KelasRpsReqDto reqDto, HttpServletRequest request) {
        Rps rps = rpsRepository.findByIdAndIsDeletedFalse(reqDto.getRpsId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Rps tidak ditemukan : " + reqDto.getRpsId()));

        if (reqDto.getKelasIds() != null && !reqDto.getKelasIds().isEmpty()) {
            List<KelasKuliah> kelasKuliahList = kelasKuliahRepository.findAllByIdAndIsDeletedFalse(reqDto.getKelasIds());
            rps.setKelasKuliahList(kelasKuliahList);
        }

        Rps saved = rpsRepository.save(rps);

        service.saveUserActivity(request, MessageKey.UPDATE_RPS);
        mapper.toDtoKelas(saved);
    }

    @Override
    public RpsResDto getRpsByKelas(UUID kelasId) {
        KelasRps kelasRps = kelasRpsRepository.findSiakRps_idBySiakKelasKuliah_IdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Rps tidak ditemukan : " + kelasId));

        return mapper.toDto(kelasRps);
    }

    @Override
    public RpsDetailResDto getOneRpsDetail(UUID id, String whichId){
        User user = userActivityService.getCurrentUser();

        RpsDetailResDto result;
        if ("mataKuliah".equals(whichId)){
            // Fetch the raw Object[] from the native query
            Object[] rawResult = rpsRepository.getRpsDetailProjectionByMataKuliahAndDosen(id, user.getSiakDosen().getId())
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "RPS detail not found for given mataKuliah and dosen."));

            // Use the RpsRawDataToDtoMapper to convert the Object[] into RpsDetailResDto
            result = rpsRawDataToDtoMapper.mapRawDataToRpsDetailResDto(rawResult); // Call the new mapper method
        } else {
            // This path uses the JPQL constructor expression and returns RpsDetailResDto directly
            result = rpsRepository.getRpsDetailById(id)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "RPS detail not found for ID: " + id));
        }

        return result;
    }
}
