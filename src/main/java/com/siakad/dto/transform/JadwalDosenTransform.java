package com.siakad.dto.transform;

import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.response.JadwalDosenResDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import com.siakad.entity.JadwalKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JadwalDosenTransform {
    JadwalKuliah toEntity(JadwalDosenReqDto dto);

    List<JadwalDto> toDto(List<JadwalKuliah> jadwalKuliahList);

    void toEntity(JadwalDosenReqDto dto, @MappingTarget JadwalKuliah entity);
}
