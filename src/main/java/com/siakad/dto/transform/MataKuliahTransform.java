package com.siakad.dto.transform;

import com.siakad.dto.request.KurikulumProdiReqDto;
import com.siakad.dto.request.MataKuliahReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.Jenjang;
import com.siakad.entity.MataKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MataKuliahTransform {
    MataKuliah toEntity(MataKuliahReqDto dto);



    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "prasyaratMataKuliah1", target = "prasyaratMataKuliah1")
    @Mapping(source = "prasyaratMataKuliah2",target = "prasyaratMataKuliah2" )
    @Mapping(source = "prasyaratMataKuliah3",target = "prasyaratMataKuliah3")
    MataKuliahResDto toDto(MataKuliah entity);

    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    MataKuliahDto mataKuliahDto(MataKuliah entity);
    List<MataKuliahDto> mataKuliahDtoList(List<MataKuliah> entityList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "kodeMataKuliah", target = "kodeMataKuliah")
    @Mapping(source = "namaMataKuliah", target = "namaMataKuliah")
    PrasyaratMataKuliahDto prasyaratMataKuliahToDto(MataKuliah entity);

    List<PrasyaratMataKuliahDto> prasyaratMataKuliahToDtoList(List<MataKuliah> entityList);

    List<MataKuliahResDto> toDtoList(List<MataKuliah> entities);

    void toEntity(MataKuliahReqDto dto, @MappingTarget MataKuliah entity);
    void toEntity(KurikulumProdiReqDto dto, @MappingTarget MataKuliah entity);
}
