package com.siakad.dto.transform;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.response.*;
import com.siakad.dto.transform.helper.KelasKuliahMapperHelper;
import com.siakad.entity.*;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KelasKuliahTranform {

    KelasKuliah toEntity(KelasKuliahReqDto dto);
    JadwalKuliah toEntity(JadwalKuliahReqDto dto);

    @Mapping(source = "siakProgramStudi", target = "programStudi")
    @Mapping(source = "siakPeriodeAkademik.namaPeriode", target = "periodeAkademik")
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    @Mapping(target = "dosen", expression = "java(helper.mapDosen(entity))")
    @Mapping(target = "jadwalMingguan", expression = "java(helper.mapHariJadwal(entity))")
    KelasKuliahResDto toDto(KelasKuliah entity, @Context KelasKuliahMapperHelper helper);

    void toEntity(JadwalKuliahReqDto dto, @MappingTarget JadwalKuliah entity);
    void toEntity(KelasKuliahReqDto dto, @MappingTarget KelasKuliah entity);

    @Mapping(source = "siakJenjang", target = "jenjang")
    ProgramStudiResDto programStudiToDto(ProgramStudi entity);
    List<ProgramStudiResDto> programStudiListToDtoList(List<ProgramStudi> programStudiList);

    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    MataKuliahDto mataKuliahDto(MataKuliah entity);
    List<MataKuliahDto> mataKuliahDtoList(List<MataKuliah> entityList);

    JenjangResDto jenjangToDto(Jenjang entity);
    List<JenjangResDto> jenjangListToDtoList(List<Jenjang> entityList);
}
