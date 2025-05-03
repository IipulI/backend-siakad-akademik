package com.siakad.dto.transform;

import com.siakad.dto.request.MataKuliahReqDto;
import com.siakad.dto.response.MataKuliahResDto;
import com.siakad.entity.MataKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MataKuliahTransform {
    MataKuliah toEntity(MataKuliahReqDto dto);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "prasyaratMataKuliah1.namaMataKuliah", target = "prasyaratMataKuliah1")
    @Mapping(source = "prasyaratMataKuliah2.namaMataKuliah",target = "prasyaratMataKuliah2" )
    @Mapping(source = "prasyaratMataKuliah3.namaMataKuliah",target = "prasyaratMataKuliah3")
    MataKuliahResDto toDto(MataKuliah entity);

    void toEntity(MataKuliahReqDto dto, @MappingTarget MataKuliah entity);
}
