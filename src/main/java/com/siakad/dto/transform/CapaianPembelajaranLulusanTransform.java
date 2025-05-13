package com.siakad.dto.transform;

import com.siakad.dto.request.CapaianPembelajaranLulusanReqDto;
import com.siakad.dto.response.CapaianPembelajaranLulusanResDto;
import com.siakad.dto.response.ProfilLulusanResDto;
import com.siakad.entity.CapaianPembelajaranLulusan;
import com.siakad.entity.ProfilLulusan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CapaianPembelajaranLulusanTransform {
    CapaianPembelajaranLulusan toEntity(CapaianPembelajaranLulusanReqDto dto);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(source = "profilLulusanList", target = "profilLulusanMapped")
    CapaianPembelajaranLulusanResDto toDto(CapaianPembelajaranLulusan entity);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    ProfilLulusanResDto toDto(ProfilLulusan entity);
    void toEntity(CapaianPembelajaranLulusanReqDto dto, @MappingTarget CapaianPembelajaranLulusan entity);

    List<ProfilLulusanResDto> toDto(List<CapaianPembelajaranLulusan> entities);
}
