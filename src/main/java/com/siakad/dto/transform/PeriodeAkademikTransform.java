package com.siakad.dto.transform;

import com.siakad.dto.request.PeriodeAkademikReqDto;
import com.siakad.dto.response.PeriodeAkademikResDto;
import com.siakad.entity.PeriodeAkademik;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PeriodeAkademikTransform {
    PeriodeAkademik toEntity(PeriodeAkademikReqDto dto);

    @Mapping(source = "siakTahunAjaran.nama", target = "tahun")
    PeriodeAkademikResDto toDto(PeriodeAkademik entity);
    void toEntity(PeriodeAkademikReqDto dto, @MappingTarget PeriodeAkademik entity);
}
