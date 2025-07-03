package com.siakad.dto.transform;

import com.siakad.dto.request.GetJadwalResDto;
import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.response.*;
import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.JadwalUjian;
import com.siakad.entity.MataKuliah;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JadwalDosenTransform {
    JadwalKuliah toEntity(JadwalDosenReqDto dto);


    List<JadwalDto> toDto(List<JadwalKuliah> jadwalKuliahList);

    void tx1oEntity(JadwalDosenReqDto dto, @MappingTarget JadwalKuliah entity);

    @Mapping(source = "siakKelasKuliah.siakMataKuliah.namaMataKuliah", target = "mataKuliah")
    @Mapping(source = "siakKelasKuliah.nama", target = "kelas")
    GetJadwalResDto toGetJadwalResDto(JadwalKuliah jadwalKuliah);
    List<GetJadwalResDto> toGetJadwalResDtoList(List<JadwalKuliah> entities);

    List<JadwalUjianResDto> toJadwalUjianDto(List<JadwalUjian> jadwalUjianList);

    GetDosenDto getDosenDto(Dosen entity);

    List<GetDosenDto> getDosenDtoList(List<Dosen> entityList);
}
