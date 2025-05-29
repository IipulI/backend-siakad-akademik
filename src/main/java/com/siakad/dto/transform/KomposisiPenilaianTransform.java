package com.siakad.dto.transform;

import com.siakad.dto.request.KomposisiPenilaianReqDto;
import com.siakad.dto.response.KomposisiPenilaianResDto;
import com.siakad.entity.KomposisiPenilaian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KomposisiPenilaianTransform {

    KomposisiPenilaian toEntity(KomposisiPenilaianReqDto dto);

    @Mapping(source = "id", target = "id")
    KomposisiPenilaianResDto toDto(KomposisiPenilaian entity);
    void toEntity(KomposisiPenilaianReqDto dto, @MappingTarget KomposisiPenilaian entity);
}
