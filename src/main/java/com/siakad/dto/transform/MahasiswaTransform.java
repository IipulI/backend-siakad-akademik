package com.siakad.dto.transform;

import com.siakad.dto.request.EditKeluargaMahasiswaReqDto;
import com.siakad.dto.request.EditMahasiswaReqDto;
import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.dto.response.KeluargaMahasiswaResDto;
import com.siakad.dto.response.MahasiswaResDto;
import com.siakad.entity.KeluargaMahasiswa;
import com.siakad.entity.Mahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MahasiswaTransform {
    Mahasiswa toEntity(MahasiswaReqDto reqDto);

    Mahasiswa toEntity(EditKeluargaMahasiswaReqDto dto);

    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "namaProgramStudi")
    @Mapping(source = "siakProgramStudi.siakJenjang.nama", target = "jenjang")
    @Mapping(source = "keluarga", target = "keluargaMahasiswaList")
    MahasiswaResDto toDto(Mahasiswa mahasiswa);

    void toEntity(EditKeluargaMahasiswaReqDto dto, @MappingTarget KeluargaMahasiswa entity);

    // Tambahan untuk edit
    void toEntity(EditMahasiswaReqDto dto, @MappingTarget Mahasiswa entity);

    KeluargaMahasiswa toEntityKeluarga(EditKeluargaMahasiswaReqDto dto);

    KeluargaMahasiswa toEntity(KeluargaMahasiswaReqDto dto);
    KeluargaMahasiswaResDto toDto(KeluargaMahasiswa entity);
    void toEntity(KeluargaMahasiswaReqDto dto, @MappingTarget KeluargaMahasiswa entity);

    // Mapping list
    List<KeluargaMahasiswa> toEntityList(List<KeluargaMahasiswaReqDto> dtoList);
    List<KeluargaMahasiswaResDto> toDtoList(List<KeluargaMahasiswa> entityList);

    default byte[] map(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert MultipartFile to byte[]", e);
        }
    }
}

