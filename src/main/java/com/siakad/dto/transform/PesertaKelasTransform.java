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

    // Corrected mappings based on your entity structure:
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.npm", target = "npm")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.nama", target = "nama")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.angkatan", target = "angkatan")
    @Mapping(source = "krsRincianMahasiswa.siakKrsMahasiswa.siakMahasiswa.siakProgramStudi", target = "programStudiResDto")
    PesertaKelas toDto(KrsRincianMahasiswa krsRincianMahasiswa);

    // This method is correctly defined to map a list
    List<PesertaKelas> toDtoList(List<KrsRincianMahasiswa> krsRincianMahasiswaList);

    // Mapper for ProgramStudi entity to ProgramStudiResDto
    @Mapping(source = "id", target = "id")
    // Map 'namaProgramStudi' from entity to 'namaProgramStudi' in DTO (same name, auto-mapped, but explicit here for clarity)
    @Mapping(source = "namaProgramStudi", target = "namaProgramStudi")
    // Map the 'siakJenjang' entity to the 'jenjang' DTO field.
    // MapStruct will automatically call the 'toJenjangResDto' method below.
    @Mapping(source = "siakJenjang", target = "jenjang")
    ProgramStudiResDto toProgramStudiResDto(ProgramStudi programStudi);

    // Mapper for Jenjang entity to JenjangResDto
    @Mapping(source = "id", target = "id")
    // Map 'nama' from Jenjang entity to 'nama' in JenjangResDto
    @Mapping(source = "nama", target = "nama")
    // Map 'jenjang' from Jenjang entity to 'jenjang' in JenjangResDto
    @Mapping(source = "jenjang", target = "jenjang")
    JenjangResDto toJenjangResDto(Jenjang jenjang);
}