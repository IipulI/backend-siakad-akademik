package com.siakad.dto.transform;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.request.JadwalUjianReqDto;
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
    JadwalUjian toEntity(JadwalUjianReqDto dto);

    @Mapping(source = "siakProgramStudi", target = "programStudi")
    @Mapping(source = "siakPeriodeAkademik.namaPeriode", target = "periodeAkademik")
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    @Mapping(target = "dosen", expression = "java(helper.mapDosen(entity))")
    @Mapping(target = "jadwalMingguan", expression = "java(helper.mapHariJadwal(entity))")
    @Mapping(target = "peserta", expression = "java(helper.hitungJumlahPeserta(entity))")
    @Mapping(target = "statusPenilaian", expression = "java(helper.tentukanStatusPenilaian(entity))")
    KelasKuliahResDto toDto(KelasKuliah entity, @Context KelasKuliahMapperHelper helper);


    KelasKuliahDto toDtoKelasKuliah(KelasKuliah entity);

    KomponenPenilaianResDto toDto(KelasKuliah entity);

    void toEntity(JadwalKuliahReqDto dto, @MappingTarget JadwalKuliah entity);
    void toEntity(KelasKuliahReqDto dto, @MappingTarget KelasKuliah entity);
    void toEntity(JadwalUjianReqDto dto, @MappingTarget JadwalUjian entity);

    @Mapping(source = "siakJenjang", target = "jenjang")
    ProgramStudiResDto programStudiToDto(ProgramStudi entity);
    List<ProgramStudiResDto> programStudiListToDtoList(List<ProgramStudi> programStudiList);

    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    MataKuliahDto mataKuliahDto(MataKuliah entity);
    List<MataKuliahDto> mataKuliahDtoList(List<MataKuliah> entityList);

    JenjangResDto jenjangToDto(Jenjang entity);
    List<JenjangResDto> jenjangListToDtoList(List<Jenjang> entityList);

    @Mapping(source = "nama", target = "nama")
    @Mapping(source = "npm", target = "npm")
    MahasiswaDto mahasiswaToDto(Mahasiswa entity);
    List<MahasiswaDto> mahasiswaListToDtoList(List<Mahasiswa> entityList);

    KomponenPenilaianResDto komponenPenilaianDto(KomposisiPenilaian entity);
    List<KomponenPenilaianResDto> komponenPenilaianListToDtoList(List<KomposisiPenilaian> entityList);
}
