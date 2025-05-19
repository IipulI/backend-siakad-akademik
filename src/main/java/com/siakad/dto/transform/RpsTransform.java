package com.siakad.dto.transform;

import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RpsTransform {

    @Mapping(source = "dosenList", target = "dosenPenyusun")
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    @Mapping(source = "siakTahunKurikulum", target = "tahunKurikulum")
    @Mapping(source = "siakProgramStudi", target = "programStudi")
    @Mapping(source = "siakPeriodeAkademik", target = "periodeAkademik")
    @Mapping(source = "kelasKuliahList", target = "kelas")
    RpsResDto toDto(Rps entity);

    KelasRpsResponseDto toDtoKelas(Rps entity);

    List<RpsResDto> toDtoList(List<Rps> entityList);

    Rps toEntity(RpsReqDto dto);

    Rps toEntityKelas(KelasRpsReqDto dto);

    void toEntity(RpsReqDto dto, @MappingTarget Rps entity);

    RpsDosenResDto dosenToDto(Dosen dosen);
    List<RpsDosenResDto> dosenListToDtoList(List<Dosen> dosenList);

    MataKuliahRpsResDto mataKuliahToDto(MataKuliah mataKuliah);
    List<MataKuliahRpsResDto> mataKuliahListToDtoList(List<MataKuliah> mataKuliahList);

    PeriodeAkademikDto periodeAkademikToDto(PeriodeAkademik periodeAkademik);
    List<PeriodeAkademikDto> periodeAkademikListToDtoList(List<PeriodeAkademik> periodeAkademikList);

    ProgramStudiDto programStudiToDto(ProgramStudi programStudi);
    List<ProgramStudiDto> programStudiListToDtoList(List<ProgramStudi> programStudiList);

    TahunKurikulumDto tahunKurikumToDto(TahunKurikulum tahunKurikulum);
    List<TahunKurikulumDto> tahunKurikumListToDtoList(List<TahunKurikulum> tahunKurikulumList);

    KelasKuliahDto kelasKuliahToDto(KelasKuliah kelasKuliah);
    List<KelasKuliahDto> kelasKuliahListToDtoList(List<KelasKuliah> kelasKuliahList);
}
