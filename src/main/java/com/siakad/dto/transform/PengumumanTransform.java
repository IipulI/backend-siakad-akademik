package com.siakad.dto.transform;

import com.siakad.dto.request.PengumumanReqDto;
import com.siakad.dto.response.PengumumanResDto;
import com.siakad.entity.Pengumuman;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PengumumanTransform {
    Pengumuman toEntity(PengumumanReqDto dto);
    @Mapping(source = "siakUser.username", target = "user")
    @Mapping(source = "createdAt", target = "tanggal")
    PengumumanResDto toDto(Pengumuman entity);
    void toEntity(PengumumanReqDto dto, @MappingTarget Pengumuman entity);
}
