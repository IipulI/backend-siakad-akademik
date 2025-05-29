package com.siakad.dto.transform;

import com.siakad.dto.request.JenjangReqDto;
import com.siakad.dto.response.JenjangResDto;
import com.siakad.entity.Jenjang;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface JenjangTransform {

    Jenjang toEntity(JenjangReqDto dto);

    @Mapping(source = "id", target = "id")
    JenjangResDto toDto(Jenjang entity);
    void toEntity(JenjangReqDto dto, @MappingTarget Jenjang entity);
}
