package com.siakad.dto.transform;

import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.response.KelasKuliahResDto;
import com.siakad.entity.KelasKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface KelasKuliahTranform {

    KelasKuliah toEntity(KelasKuliahReqDto dto);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
    @Mapping(source = "siakPeriodeAkademik.namaPeriode", target = "periodeAkademik")
    @Mapping(source = "siakMataKuliah.namaMataKuliah", target = "mataKuliah")
    KelasKuliahResDto toDto(KelasKuliah entity);

    void toEntity(KelasKuliahReqDto dto, @MappingTarget KelasKuliah entity);

}
