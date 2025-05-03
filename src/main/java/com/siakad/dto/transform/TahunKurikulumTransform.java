package com.siakad.dto.transform;


import com.siakad.dto.request.TahunKurikulumReqDto;
import com.siakad.dto.response.TahunKurikulumResDto;
import com.siakad.entity.TahunKurikulum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TahunKurikulumTransform {
    TahunKurikulum toEntity(TahunKurikulumReqDto dto);

    @Mapping(source = "siakPeriodeAkademik.namaPeriode", target = "mulaiBerlaku")
    TahunKurikulumResDto toDto(TahunKurikulum entity);
    void toEntity(TahunKurikulumReqDto dto, @MappingTarget TahunKurikulum entity);
}
