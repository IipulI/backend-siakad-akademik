package com.siakad.service.impl;

import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.response.ManajemenOBEResDto;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.service.ProgramStudiSpecification;
import com.siakad.repository.CapaianMataKuliahRepository;
import com.siakad.repository.CapaianPembelajaranLulusanRepository;
import com.siakad.repository.ProfilLulusanRepository;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.service.AkademikService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class AkademikServiceImpl implements AkademikService {

    private final ProgramStudiRepository programStudiRepository;
    private final ProfilLulusanRepository profilLulusanRepository;
    private final CapaianPembelajaranLulusanRepository cplRepository;
    private final CapaianMataKuliahRepository cpmkRepository;

    @Override
    public List<ManajemenOBEResDto> getStatusOverview(String tahunKurikulum, String namaProdi, String namaJenjang) {
        Specification<ProgramStudi> spec = ProgramStudiSpecification.build(tahunKurikulum, namaProdi, namaJenjang);
        List<ProgramStudi> programStudiList = programStudiRepository.findAll(spec);

        if (programStudiList.isEmpty()) {
            return Collections.emptyList();
        }
        List<UUID> prodiIds = programStudiList.stream().map(ProgramStudi::getId).collect(Collectors.toList());

        // Ambil semua status dalam 4 query batch yang efisien
        Set<UUID> prodiWithPl = profilLulusanRepository.findProdiIdsWithRelations(prodiIds);
        Set<UUID> prodiWithCpl = cplRepository.findProdiIdsWithRelations(prodiIds);
        Set<UUID> prodiWithPlCpl = cplRepository.findProdiIdsWithPemetaanPlCpl(prodiIds);
        Set<UUID> prodiWithCpmk = cpmkRepository.findProdiIdsWithRelations(prodiIds);

        // Rakit DTO dengan pengecekan di memori (sangat cepat)
        return programStudiList.stream().map(prodi -> {
            UUID currentId = prodi.getId();
            return ManajemenOBEResDto.builder()
                    .id(prodi.getId())
                    .kodeProgramStudi(prodi.getKodeProgramStudi())
                    .programStudi(prodi.getNamaProgramStudi())
                    .jenjang(JenjangResDto.fromEntity(prodi.getSiakJenjang()))
                    .statusPl(prodiWithPl.contains(currentId))
                    .statusCpl(prodiWithCpl.contains(currentId))
                    .statusPlCpl(prodiWithPlCpl.contains(currentId))
                    .statusCpmk(prodiWithCpmk.contains(currentId))
                    .build();
        }).collect(Collectors.toList());
    }
}
