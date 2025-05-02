package com.siakad.dto.transform;

import com.siakad.dto.request.TahunAjaranReqDto;
import com.siakad.dto.response.TahunAjaranResDto;
import com.siakad.entity.TahunAjaran;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TahunAjaranTransform {
    TahunAjaran toEntity(TahunAjaranReqDto dto);
    TahunAjaranResDto toDto(TahunAjaran entity);
    void toEntity(TahunAjaranReqDto dto, @MappingTarget TahunAjaran entity);
}
