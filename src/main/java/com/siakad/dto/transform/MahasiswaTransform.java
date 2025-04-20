package com.siakad.dto.transform;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.entity.Mahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MahasiswaTransform {
    Mahasiswa toEntity(MahasiswaReqDto dto);
    MahasiswaResDto toDto(Mahasiswa entity);
    void toEntity(MahasiswaReqDto dto,  @MappingTarget Mahasiswa entity);
}
