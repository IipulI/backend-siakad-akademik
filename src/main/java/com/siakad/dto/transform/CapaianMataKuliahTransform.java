package com.siakad.dto.transform;

import com.siakad.dto.request.CapaianMataKuliahReqDto;
import com.siakad.dto.response.CapaianMataKuliahResDto;
import com.siakad.dto.response.CapaianPembelajaranLulusanResDto;
import com.siakad.dto.response.ProfilLulusanResDto;
import com.siakad.entity.CapaianMataKuliah;
import com.siakad.entity.CapaianPembelajaranLulusan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CapaianMataKuliahTransform {
    CapaianMataKuliah toEntity(CapaianMataKuliahReqDto dto);

    @Mapping(source = "siakMataKuliah.namaMataKuliah", target = "mataKuliah")
    @Mapping(source = "capaianPembelajaranLulusanList", target = "capaianPembelajaranMapped")
    CapaianMataKuliahResDto toDto(CapaianMataKuliah entity);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
    @Mapping(target = "profilLulusanMapped", ignore = true)
    CapaianPembelajaranLulusanResDto toDto(CapaianPembelajaranLulusan entity);

    List<ProfilLulusanResDto> toDto(List<CapaianPembelajaranLulusan> entities);

    void toEntity(CapaianMataKuliahReqDto dto, @MappingTarget CapaianMataKuliah entity);
}
