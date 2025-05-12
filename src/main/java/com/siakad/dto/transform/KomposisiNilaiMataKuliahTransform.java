package com.siakad.dto.transform;

import com.siakad.dto.request.KomposisiNilaiMataKuliahReqDto;
import com.siakad.dto.response.KomposisiNilaiMataKuliahResDto;
import com.siakad.entity.KomposisiNilaiMataKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KomposisiNilaiMataKuliahTransform {

    KomposisiNilaiMataKuliah toEntity(KomposisiNilaiMataKuliahReqDto dto);

    @Mapping(source = "siakKomposisiNilai.id", target = "siakKomposisiNilaiId")
    @Mapping(source = "siakKomposisiNilai.nama", target = "nama")
    @Mapping(source = "siakKomposisiNilai.persentase", target = "persentase")
    KomposisiNilaiMataKuliahResDto toDto(KomposisiNilaiMataKuliah entity);
    void toEntity(KomposisiNilaiMataKuliahReqDto dto, @MappingTarget KomposisiNilaiMataKuliah entity);
}
