package com.siakad.dto.transform;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.response.DosenDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JadwalKuliahTransform {

    JadwalKuliah toEntity(JadwalKuliahReqDto dto);

    @Mapping(source = "siakRuangan.namaRuangan", target = "namaRuangan")
    JadwalKuliahResDto toDto(JadwalKuliah entity);

    List<JadwalKuliahResDto> toDtoList(List<JadwalKuliah> entities);

    void toEntity(JadwalKuliahReqDto dto, @MappingTarget JadwalKuliah entity);

    JadwalDto toJadwalDto(JadwalKuliah jadwal);

    default DosenDto toDosenPengajarDto(Dosen dosen, List<JadwalKuliah> jadwalList) {
        List<JadwalDto> jadwalDtoList = jadwalList.stream()
                .map(this::toJadwalDto)
                .collect(Collectors.toList());

        return new DosenDto(
                dosen.getId(),
                dosen.getNama(),
                jadwalDtoList
        );
    }
}
