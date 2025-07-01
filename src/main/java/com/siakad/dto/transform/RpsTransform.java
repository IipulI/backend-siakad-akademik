package com.siakad.dto.transform;

import com.siakad.dto.request.KelasRpsReqDto;
import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RpsTransform {

    @Mapping(source = "dosenList", target = "dosenPenyusun")
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    @Mapping(source = "siakTahunKurikulum", target = "tahunKurikulum")
    @Mapping(source = "siakProgramStudi", target = "programStudi")
    @Mapping(source = "siakPeriodeAkademik", target = "periodeAkademik")
    @Mapping(source = "kelasKuliahList", target = "kelas")
    RpsResDto toDto(Rps entity);

    @Mapping(source = "siakRps.dosenList", target = "dosenPenyusun")
    @Mapping(source = "siakRps.siakMataKuliah", target = "mataKuliah")
    @Mapping(source = "siakRps.siakTahunKurikulum", target = "tahunKurikulum")
    @Mapping(source = "siakRps.siakProgramStudi", target = "programStudi")
    @Mapping(source = "siakRps.siakPeriodeAkademik", target = "periodeAkademik")
    @Mapping(source = "siakRps.kelasKuliahList", target = "kelas")
    @Mapping(source = "siakRps.tanggalPenyusun", target = "tanggalPenyusun")
    @Mapping(source = "siakRps.deskripsiMataKuliah", target = "deskripsiMataKuliah")
    @Mapping(source = "siakRps.tujuanMataKuliah", target = "tujuanMataKuliah")
    @Mapping(source = "siakRps.materiPembelajaran", target = "materiPembelajaran")
    @Mapping(source = "siakRps.pustakaUtama", target = "pustakaUtama")
    @Mapping(source = "siakRps.pustakaPendukung", target = "pustakaPendukung")
    @Mapping(source = "id", target = "id", qualifiedByName = "mapKelasRpsIdToUUID")
    RpsResDto toDto(KelasRps entity);

    RpsDetailResDto toDetailDto(Rps entity);

    KelasRpsResponseDto toDtoKelas(Rps entity);

    List<RpsResDto> toDtoList(List<Rps> entityList);

    Rps toEntity(RpsReqDto dto);

    Rps toEntityKelas(KelasRpsReqDto dto);

    void toEntity(RpsReqDto dto, @MappingTarget Rps entity);

    RpsDosenResDto dosenToDto(Dosen dosen);
    List<RpsDosenResDto> dosenListToDtoList(List<Dosen> dosenList);

    @Mapping(target = "sks", source = "mataKuliah", qualifiedByName = "totalSks")
    @Mapping(source = "semester", target = "semester")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "kurikulum")
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

    @Named("totalSks")
    default Integer totalSks(MataKuliah mataKuliah) {
        return mataKuliah.getSksPraktikum() + mataKuliah.getSksTatapMuka();
    }

    @Named("mapKelasRpsIdToUUID")
    default UUID mapKelasRpsIdToUUID(KelasRpsId id) {
        return id != null ? id.getSiakRpsId() : null;
    }
}
