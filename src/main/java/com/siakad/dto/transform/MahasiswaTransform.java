package com.siakad.dto.transform;

import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.KeluargaMahasiswaResDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.entity.KeluargaMahasiswa;
import com.siakad.entity.Mahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MahasiswaTransform {
    Mahasiswa toEntity(MahasiswaReqDto dto);
    @Mapping(source = "siakProgramStudi.id", target = "siakProgramStudiId")
    @Mapping(source = "keluarga", target = "keluargaMahasiswaList")
    MahasiswaResDto toDto(Mahasiswa entity);
    void toEntity(MahasiswaReqDto dto, @MappingTarget Mahasiswa entity);

    KeluargaMahasiswa toEntity(KeluargaMahasiswaReqDto dto);
    KeluargaMahasiswaResDto toDto(KeluargaMahasiswa entity);
    void toEntity(KeluargaMahasiswaReqDto dto,  @MappingTarget KeluargaMahasiswa entity);
}
