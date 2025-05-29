package com.siakad.dto.transform;

import com.siakad.dto.request.PembimbingAkademikReqDto;
import com.siakad.dto.response.PembimbingAkademikResDto;
import com.siakad.entity.PembimbingAkademik;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PembimbingAkademikTransform {
    PembimbingAkademik toEntity(PembimbingAkademikReqDto dto);

//    @Mapping(source = "siakMahasiswa.nama", target = "mahasiswa")
//    @Mapping(source = "siakMahasiswa.angkatan", target = "angkatan")
//    @Mapping(source = "siakMahasiswa.siakProgramStudi.namaProgramStudi", target = "programStudi")
//    @Mapping(source = "siakMahasiswa.siakProgramStudi.siakFakultas.namaFakultas", target = "unitKerja")
    PembimbingAkademikResDto toDto(PembimbingAkademik entity);

    void toEntity(PembimbingAkademikReqDto dto, @MappingTarget PembimbingAkademik entity);
}
