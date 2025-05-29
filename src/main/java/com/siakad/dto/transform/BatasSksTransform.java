package com.siakad.dto.transform;

import com.siakad.dto.request.BatasSksReqDto;
import com.siakad.dto.response.BatasSksResDto;
import com.siakad.entity.BatasSks;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BatasSksTransform {

    BatasSks toEntity(BatasSksReqDto dto);

    @Mapping(source = "siakJenjang.nama", target = "jenjang")
    BatasSksResDto toDto(BatasSks entity);
    void toEntity(BatasSksReqDto dto, @MappingTarget BatasSks entity);
}
