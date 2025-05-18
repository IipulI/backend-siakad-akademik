package com.siakad.dto.transform;

import com.siakad.dto.response.DosenDto;
import com.siakad.entity.Dosen;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DosenTransform {

//    @Mapping(source = "id", target = "siakDosenId")
//    @Mapping(source = "nama", target = "nama")
//    DosenDto toDto(Dosen dosen, @Context List<JadwalKuliahResDto> jadwalKuliah);
}
