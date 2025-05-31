package com.siakad.dto.transform;

import com.siakad.dto.response.KomponenPenilaianResDto;
import com.siakad.dto.response.MahasiswaDto;
import com.siakad.dto.response.PenilaianKelasResDto;
import com.siakad.dto.transform.helper.RincianKrsMappeHelper;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.KomposisiPenilaian;
import com.siakad.entity.Mahasiswa;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class PenilaianTransform {

    @Autowired
    protected RincianKrsMappeHelper rincianKrsMappeHelper;

    public abstract PenilaianKelasResDto toDto(KelasKuliah kelasKuliah);

    @Mapping(source = "nama", target = "nama")
    @Mapping(source = "npm", target = "npm")
    @Mapping(target = "tugas", ignore = true)
    @Mapping(target = "uts", ignore = true)
    @Mapping(target = "kehadiran", ignore = true)
    @Mapping(target = "uas", ignore = true)
    @Mapping(target = "nilai", ignore = true)
    @Mapping(target = "grade", ignore = true)
    public abstract MahasiswaDto mahasiswaToDto(Mahasiswa mahasiswa);

    public abstract List<MahasiswaDto> mahasiswaListToDtoList(List<Mahasiswa> mahasiswaList);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "nama", target = "nama")
    @Mapping(source = "persentase", target = "persentase")
    public abstract KomponenPenilaianResDto komponenPenilaianDto(KomposisiPenilaian entity);
    public abstract List<KomponenPenilaianResDto> komponenPenilaianListToDtoList(List<KomposisiPenilaian> entityList);

}
