package com.siakad.dto.transform;

import com.siakad.dto.response.HasilStudiDto;
import com.siakad.dto.response.RincianKrsDto;
import com.siakad.entity.HasilStudi;
import com.siakad.entity.KrsRincianMahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HasilStudiTransform {

    @Mapping(source = "ips", target = "ips")
    @Mapping(target = "rincianKrsDto", ignore = true)
    HasilStudiDto toDto(HasilStudi hasilStudi);

    @Mapping(source = "siakKelasKuliah.siakMataKuliah.namaMataKuliah", target = "namaMataKuliah")
    @Mapping(source = "siakKelasKuliah.siakMataKuliah.kodeMataKuliah", target = "kodeMataKuliah")
    @Mapping(source = "siakKelasKuliah.siakMataKuliah.sksTatapMuka", target = "sks")
    @Mapping(source = "hurufMutu", target = "hurufMutu")
    @Mapping(source = "nilaiAkhir", target = "nilaiMutu")
    @Mapping(source = "nilai", target = "bobot")
    RincianKrsDto rincianKrsToDto(KrsRincianMahasiswa entity);
    List<RincianKrsDto> rincianKrsListToDtoList(List<KrsRincianMahasiswa> entityList);

    default HasilStudiDto hasilStudiToDtoWithRincian(HasilStudi hasilStudi, List<KrsRincianMahasiswa> rincianList) {
        HasilStudiDto dto = toDto(hasilStudi);
        dto.setRincianKrsDto(rincianKrsListToDtoList(rincianList));
        return dto;
    }
}
