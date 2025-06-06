package com.siakad.dto.transform;

import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.dto.response.RuanganResDto;
import com.siakad.entity.Jenjang;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.Ruangan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProgramStudiTransform {

    @Mapping(source = "siakJenjang", target = "jenjang")
    ProgramStudiResDto toDto(ProgramStudi entity);

    RuanganResDto toDto(Ruangan entity);

    JenjangResDto jenjangToDto(Jenjang entity);
    List<JenjangResDto> jenjangListToDtoList(List<Jenjang> entityList);
}
