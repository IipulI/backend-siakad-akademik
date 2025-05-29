package com.siakad.service.impl;

import com.siakad.dto.request.KomposisiNilaiMataKuliahReqDto;
import com.siakad.dto.request.KomposisiPenilaianReqDto;
import com.siakad.dto.response.KomposisiNilaiMataKuliahResDto;
import com.siakad.dto.response.KomposisiPenilaianResDto;
import com.siakad.dto.transform.KomposisiNilaiMataKuliahTransform;
import com.siakad.dto.transform.KomposisiPenilaianTransform;
import com.siakad.entity.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.KomposisiNilaiMataKuliahRepository;
import com.siakad.repository.KomposisiPenilaianRepository;
import com.siakad.repository.MataKuliahRepository;
import com.siakad.repository.TahunKurikulumRepository;
import com.siakad.service.KomposisiPenilaianService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KomposisiPenilaianServiceImpl implements KomposisiPenilaianService {

    private final KomposisiPenilaianRepository komposisiPenilaianRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final UserActivityService service;
    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final KomposisiNilaiMataKuliahRepository komposisiNilaiMataKuliahRepository;
    private final KomposisiPenilaianTransform mapper;
    private final KomposisiNilaiMataKuliahTransform mapperMataKuliah;

    @Override
    public List<KomposisiPenilaianResDto> getAll() {
        return komposisiPenilaianRepository.findAllByIsDeletedFalse()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public KomposisiPenilaianResDto save(KomposisiPenilaianReqDto request,
                                         HttpServletRequest servletRequest) {

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(request.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun kurikulum tidak ditemukkan : " + request.getSiakTahunKurikulumId() ));

        KomposisiPenilaian komposisiPenilaian = mapper.toEntity(request);
        komposisiPenilaian.setSiakTahunKurikulum(tahunKurikulum);
        komposisiPenilaian.setIsDeleted(false);
        KomposisiPenilaian saved = komposisiPenilaianRepository.save(komposisiPenilaian);
        service.saveUserActivity(servletRequest, MessageKey.CREATE_KOMPOSISI_PENILAIAN);
        return mapper.toDto(saved);
    }

    @Override
    public KomposisiPenilaianResDto getOne(UUID id) {
        KomposisiPenilaian komposisiPenilaian = komposisiPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komposisi Penilaian tidak ditemukkan : " + id ));

        return mapper.toDto(komposisiPenilaian);
    }

    @Override
    public KomposisiPenilaianResDto update(KomposisiPenilaianReqDto request, UUID id, HttpServletRequest servletRequest) {
        KomposisiPenilaian komposisiPenilaian = komposisiPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komposisi Penilaian tidak ditemukkan : " + id ));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(request.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun kurikulum tidak ditemukan : " + id ));

        mapper.toEntity(request, komposisiPenilaian);
        komposisiPenilaian.setSiakTahunKurikulum(tahunKurikulum);
        komposisiPenilaian.setIsDeleted(false);
        komposisiPenilaian.setUpdatedAt(LocalDateTime.now());
        komposisiPenilaianRepository.save(komposisiPenilaian);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KOMPOSISI_PENILAIAN);
        return mapper.toDto(komposisiPenilaian);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        KomposisiPenilaian komposisiPenilaian = komposisiPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun kurikulum tidak ditemukkan : " + id ));

        komposisiPenilaian.setIsDeleted(true);
        KomposisiPenilaian saved = komposisiPenilaianRepository.save(komposisiPenilaian);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_KOMPOSISI_PENILAIAN);

        mapper.toDto(saved);
    }

    @Transactional
    @Override
    public KomposisiNilaiMataKuliahResDto saveKomposisiNilaiMataKuliah(KomposisiNilaiMataKuliahReqDto dto, HttpServletRequest servletRequest) {
        KomposisiNilaiMataKuliahId id = new KomposisiNilaiMataKuliahId(
                dto.getSiakMataKuliahId(),
                dto.getSiakKomposisiNilaiId()
        );

        if(komposisiNilaiMataKuliahRepository.existsById(id)) {
            throw new ApplicationException(ExceptionType.BAD_REQUEST, "Data sudah ada!");
        }

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(dto.getSiakMataKuliahId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata kuliah tidak ditemukan"));

        KomposisiPenilaian komposisiPenilaian = komposisiPenilaianRepository.findByIdAndIsDeletedFalse(dto.getSiakKomposisiNilaiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Komposisi nilai tidak ditemukan"));

        // Buat entity manual, jangan pakai mapper
        KomposisiNilaiMataKuliah entity = new KomposisiNilaiMataKuliah();
        entity.setId(id);
        entity.setSiakMataKuliah(mataKuliah);
        entity.setSiakKomposisiNilai(komposisiPenilaian);
        entity.setIsDeleted(false);

        komposisiNilaiMataKuliahRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.CREATE_KOMPOSISI_NILAI_MATA_KULIAH);

        return mapperMataKuliah.toDto(entity); // mapper ke response masih boleh
    }



    @Override
    public List<KomposisiNilaiMataKuliahResDto> getAllKomposisiNilaiMataKuliah(UUID id) {
        return komposisiNilaiMataKuliahRepository.findAllBySiakMataKuliah_IdAndIsDeletedFalse(id)
                .stream()
                .map(mapperMataKuliah::toDto)
                .collect(Collectors.toList());
    }
}
