package com.siakad.dto.transform;

import com.siakad.dto.request.KomponenPenilaianReqDto;
import com.siakad.dto.response.KomponenPenilaianResDto;
import com.siakad.entity.KomponenPenilaian;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KomponenPenilaianTransform {
    KomponenPenilaian toEntity(KomponenPenilaianReqDto dto);
    KomponenPenilaianResDto toDto(KomponenPenilaian entity);
    void toEntity(KomponenPenilaianReqDto dto, @MappingTarget KomponenPenilaian entity);
}
