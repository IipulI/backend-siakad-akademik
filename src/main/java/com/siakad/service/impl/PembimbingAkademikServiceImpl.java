package com.siakad.service.impl;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import com.siakad.dto.transform.PembimbingAkademikTransform;
import com.siakad.dto.transform.helper.PembimbingAkademikHelper;
import com.siakad.entity.*;
import com.siakad.entity.service.MahasiswaPembimbingAkademikSpecification;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final KrsMahasiswaRepository krsMahasiswaRepository;
    private final HasilStudiRepository hasilStudiRepository;
    private final BatasSksRepository batasSksRepository;

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

    @Override
    public Page<PembimbingAkademikResDto> getAllPaginated(
            UUID programStudiId, UUID periodeAkademikId, UUID dosenId, String namaMahasiswa,
            String angkatan, String statusMahasiswa, String statusKrs,
            Boolean hasPembimbing, Pageable pageable) {

        // Build the dynamic specification with the new consolidated filter
        Specification<Mahasiswa> spec = MahasiswaPembimbingAkademikSpecification.build(programStudiId, periodeAkademikId, dosenId,
                namaMahasiswa, angkatan, statusMahasiswa, statusKrs, hasPembimbing);

        Page<Mahasiswa> mahasiswaPage = mahasiswaRepository.findAll(spec, pageable);

        if (mahasiswaPage.isEmpty()) {
            return Page.empty();
        }

        List<Mahasiswa> mahasiswaList = mahasiswaPage.getContent();
        List<UUID> mahasiswaIds = mahasiswaList.stream().map(Mahasiswa::getId).collect(Collectors.toList());

        // The rest of the logic remains the same...
        Map<UUID, List<BatasSks>> batasSksRulesMap = new HashMap<>();
        if (!mahasiswaList.isEmpty()) {
            Map<UUID, List<Mahasiswa>> studentsByJenjang = mahasiswaList.stream()
                    .collect(Collectors.groupingBy(m -> m.getSiakProgramStudi().getSiakJenjang().getId()));
            for (UUID jenjangId : studentsByJenjang.keySet()) {
                batasSksRulesMap.put(jenjangId, batasSksRepository.findBySiakJenjangId(jenjangId));
            }
        }

        Map<UUID, KrsMahasiswa> krsMap = krsMahasiswaRepository.findByMahasiswaIdsAndPeriodeId(mahasiswaIds, periodeAkademikId)
                .stream()
                .collect(Collectors.toMap(krs -> krs.getSiakMahasiswa().getId(), Function.identity()));

        Map<UUID, PembimbingAkademik> paMap = pembimbingAkademikRepository.findBySiakMahasiswaIdInAndSiakPeriodeAkademikId(mahasiswaIds, periodeAkademikId)
                .stream()
                .collect(Collectors.toMap(pa -> pa.getSiakMahasiswa().getId(), Function.identity()));

        Map<UUID, HasilStudi> hasilStudiMap = hasilStudiRepository.findLatestByMahasiswaIds(mahasiswaIds)
                .stream()
                .collect(Collectors.toMap(hs -> hs.getSiakMahasiswa().getId(), Function.identity()));

        List<PembimbingAkademikResDto> dtoList = mahasiswaList.stream().map(mahasiswa -> {
            PembimbingAkademikResDto dto = new PembimbingAkademikResDto();
            dto.setMahasiswa(mahasiswa.getNama());
            dto.setAngkatan(mahasiswa.getAngkatan());
            dto.setStatusMahasiswa(mahasiswa.getStatusMahasiswa());
            dto.setSemester(mahasiswa.getSemester());

            HasilStudi hasilStudi = hasilStudiMap.get(mahasiswa.getId());
            if (hasilStudi != null) {
                BigDecimal ips = hasilStudi.getIps();
                dto.setIps(ips);
                dto.setIpk(hasilStudi.getIpk());
                dto.setTotalSks(hasilStudi.getSksLulus());

                UUID jenjangId = mahasiswa.getSiakProgramStudi().getSiakJenjang().getId();
                List<BatasSks> rules = batasSksRulesMap.getOrDefault(jenjangId, Collections.emptyList());
                dto.setBatasSks(findBatasSks(ips, rules));
            } else {
                dto.setIps(BigDecimal.ZERO);
                dto.setIpk(BigDecimal.ZERO);
                dto.setTotalSks(0);
                dto.setBatasSks(21);
            }

            KrsMahasiswa krs = krsMap.get(mahasiswa.getId());
            dto.setStatusDiajukan(krs != null && !"Diajukan".equalsIgnoreCase(krs.getStatus()));
            dto.setStatusDisetujui(krs != null && "Disetujui".equalsIgnoreCase(krs.getStatus()));

            PembimbingAkademik pa = paMap.get(mahasiswa.getId());
            Dosen dosen = (pa != null) ? pa.getSiakDosen() : null;
            dto.setPembimbingAkademik((dosen != null) ? String.format("%s - %s", dosen.getNidn(), dosen.getNama()) : "Belum Ditentukan");

            return dto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, mahasiswaPage.getTotalElements());
    }

    private Integer findBatasSks(BigDecimal ips, List<BatasSks> rules) {
        if (ips == null) return 0;
        for (BatasSks rule : rules) {
            if (ips.compareTo(rule.getIpsMin()) >= 0 && ips.compareTo(rule.getIpsMax()) <= 0) {
                return rule.getBatasSks();
            }
        }
        return 0;
    }

}
