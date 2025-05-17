package com.siakad.dto.transform;

import com.siakad.dto.request.RpsReqDto;
import com.siakad.dto.response.MataKuliahResDto;
import com.siakad.dto.response.RpsDosenResDto;
import com.siakad.dto.response.RpsResDto;
import com.siakad.entity.Dosen;
import com.siakad.entity.MataKuliah;
import com.siakad.entity.Rps;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RpsTransform {

    @Mapping(source = "siakProgramStudi.nama", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "siakPeriodeAkademik.periode", target = "periodeAkademik")
    @Mapping(source = "dosenList", target = "dosenPenyusun")
    @Mapping(source = "siakMataKuliah", target = "mataKuliah")
    RpsResDto toDto(Rps entity);

    List<RpsResDto> toDtoList(List<Rps> entityList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "siakProgramStudi", ignore = true)
    @Mapping(target = "siakTahunKurikulum", ignore = true)
    @Mapping(target = "siakMataKuliah", ignore = true)
    @Mapping(target = "siakPeriodeAkademik", ignore = true)
    @Mapping(target = "dosenList", ignore = true)
    @Mapping(target = "dokumenRps", ignore = true)
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Rps toEntity(RpsReqDto dto);

    RpsDosenResDto dosenToDto(Dosen dosen);

    List<RpsDosenResDto> dosenListToDtoList(List<Dosen> dosenList);

    MataKuliahResDto mataKuliahToDto(MataKuliah mataKuliah);
    List<MataKuliahResDto> mataKuliahListToDtoList(List<MataKuliah> mataKuliahList);
}
