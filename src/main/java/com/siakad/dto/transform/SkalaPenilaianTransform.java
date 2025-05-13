package com.siakad.dto.transform;

import com.siakad.dto.request.SkalaPenilaianReqDto;
import com.siakad.dto.response.SkalaPenilaianResDto;
import com.siakad.entity.SkalaPenilaian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SkalaPenilaianTransform {

    SkalaPenilaian toEntity(SkalaPenilaianReqDto dto);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunAjaran.tahun", target = "tahunAjaran")
    SkalaPenilaianResDto toDto(SkalaPenilaian entity);

    void toEntity(SkalaPenilaianReqDto dto, @MappingTarget SkalaPenilaian entity);
}
