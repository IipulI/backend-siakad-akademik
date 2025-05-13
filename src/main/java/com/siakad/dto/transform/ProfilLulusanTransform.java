package com.siakad.dto.transform;

import com.siakad.dto.request.ProfilLulusanReqDto;
import com.siakad.dto.response.ProfilLulusanResDto;
import com.siakad.entity.ProfilLulusan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfilLulusanTransform {
    ProfilLulusan toEntity(ProfilLulusanReqDto dto);

    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    ProfilLulusanResDto toDto(ProfilLulusan entity);

    void toEntity(ProfilLulusanReqDto dto, @MappingTarget ProfilLulusan entity);
}
