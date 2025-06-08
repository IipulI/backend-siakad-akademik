package com.siakad.dto.transform;

import com.siakad.dto.response.HasilStudiDto;
import com.siakad.dto.response.RincianKrsDto;
import com.siakad.dto.response.TranskipDto;
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

    @Mapping(source = "ipk", target = "ipk")
    @Mapping(target = "totalSks", ignore = true)
    @Mapping(target = "rincianKrsDto", ignore = true)
    TranskipDto toDtoTranskip(HasilStudi hasilStudi);

    @Mapping(source = "siakKelasKuliah.siakMataKuliah.namaMataKuliah", target = "namaMataKuliah")
    @Mapping(source = "siakKelasKuliah.siakMataKuliah.kodeMataKuliah", target = "kodeMataKuliah")
    @Mapping(target = "sks", expression = "java(calculateTotalSks(entity))")
    @Mapping(source = "hurufMutu", target = "hurufMutu")
    @Mapping(source = "angkaMutu", target = "angkaMutu")
    @Mapping(source = "nilaiAkhir", target = "jumlahAngkaMutu")
    RincianKrsDto rincianKrsToDto(KrsRincianMahasiswa entity);

    List<RincianKrsDto> rincianKrsListToDtoList(List<KrsRincianMahasiswa> entityList);

    default HasilStudiDto hasilStudiToDtoWithRincian(HasilStudi hasilStudi, List<KrsRincianMahasiswa> rincianList) {
        HasilStudiDto dto = toDto(hasilStudi);
        dto.setRincianKrsDto(rincianKrsListToDtoList(rincianList));
        return dto;
    }

    default TranskipDto hasilStudiToDtoTranskip(HasilStudi hasilStudi, List<KrsRincianMahasiswa> rincianList) {
        TranskipDto dto = toDtoTranskip(hasilStudi);
        List<RincianKrsDto> rincianDtos = rincianKrsListToDtoList(rincianList);
        dto.setRincianKrsDto(rincianDtos);

       int totalSks = rincianDtos.stream()
                .mapToInt(r -> r.getSks() != null ? r.getSks() : 0)
                .sum();
        dto.setTotalSks(totalSks);
        return dto;
    }

    default Integer calculateTotalSks(KrsRincianMahasiswa entity) {
        Integer tatapMuka = 0;
        Integer praktikum = 0;
        if (entity.getSiakKelasKuliah() != null && entity.getSiakKelasKuliah().getSiakMataKuliah() != null) {
            tatapMuka = entity.getSiakKelasKuliah().getSiakMataKuliah().getSksTatapMuka();
            praktikum = entity.getSiakKelasKuliah().getSiakMataKuliah().getSksPraktikum();
        }
        return (tatapMuka != null ? tatapMuka : 0) + (praktikum != null ? praktikum : 0);
    }
}
