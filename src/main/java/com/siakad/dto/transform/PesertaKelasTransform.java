package com.siakad.dto.transform;

import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.response.PesertaKelas;
import com.siakad.dto.response.ProgramStudiResDto;
import com.siakad.entity.Jenjang;
import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.entity.ProgramStudi; // Don't forget to import ProgramStudi if you haven't

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PesertaKelasTransform {

    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.npm", target = "npm")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.nama", target = "nama")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.angkatan", target = "angkatan")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.siakProgramStudi", target = "programStudiResDto")
    PesertaKelas toDto(KrsRincianMahasiswa krsRincianMahasiswa);

    List<PesertaKelas> toDtoList(List<KrsRincianMahasiswa> krsRincianMahasiswaList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "namaProgramStudi", target = "namaProgramStudi")
    @Mapping(source = "siakJenjang", target = "jenjang")
    ProgramStudiResDto toProgramStudiResDto(ProgramStudi programStudi);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nama", target = "nama")
    @Mapping(source = "jenjang", target = "jenjang")
    JenjangResDto toJenjangResDto(Jenjang jenjang);
}