package com.siakad.dto.transform;

import com.siakad.dto.request.GetJadwalResDto;
import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.response.JadwalDosenResDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import com.siakad.dto.response.JadwalUjianResDto;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.JadwalUjian;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JadwalDosenTransform {
    JadwalKuliah toEntity(JadwalDosenReqDto dto);


    List<JadwalDto> toDto(List<JadwalKuliah> jadwalKuliahList);

    void toEntity(JadwalDosenReqDto dto, @MappingTarget JadwalKuliah entity);

    @Mapping(source = "siakKelasKuliah.siakMataKuliah.namaMataKuliah", target = "mataKuliah")
    @Mapping(source = "siakKelasKuliah.nama", target = "kelas")
    GetJadwalResDto toGetJadwalResDto(JadwalKuliah jadwalKuliah);
    List<GetJadwalResDto> toGetJadwalResDtoList(List<JadwalKuliah> entities);

    // Jadwal ujian
    List<JadwalUjianResDto> toJadwalUjianDto(List<JadwalUjian> jadwalUjianList);
}
