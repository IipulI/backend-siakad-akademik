    package com.siakad.dto.transform;

    import com.siakad.dto.request.KrsReqDto;
    import com.siakad.dto.response.KrsResDto;
    import com.siakad.dto.response.MataKuliahDto;
    import com.siakad.dto.response.MataKuliahResDto;
    import com.siakad.dto.transform.helper.JadwalKuliahMapperHelper;
    import com.siakad.entity.JadwalKuliah;
    import com.siakad.entity.KrsMahasiswa;
    import com.siakad.entity.KrsRincianMahasiswa;
    import com.siakad.entity.MataKuliah;
    import org.mapstruct.AfterMapping;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.MappingTarget;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import java.util.List;

    @Mapper(componentModel = "spring")
    @Component
    public abstract class KrsTransform {

        @Autowired
        private JadwalKuliahMapperHelper jadwalKuliahHelper;

    //    KrsMahasiswa toEntity(KrsReqDto dto);
        public abstract KrsRincianMahasiswa toEntityRincian(KrsReqDto dto);

        @Mapping(source = "siakKelasKuliah.siakMataKuliah", target = "mataKuliah")
        @Mapping(source = "siakKelasKuliah.siakMataKuliah.nilaiMin", target = "riwayatMatakuliah")
        @Mapping(source = "siakKelasKuliah.nama", target = "namaKelas")
        @Mapping(target = "hari", ignore = true)
        @Mapping(target = "jamMulai", ignore = true)
        @Mapping(target = "jamSelesai", ignore = true)
        @Mapping(target = "dosenPengajar", ignore = true)
        public abstract KrsResDto toDto(KrsRincianMahasiswa entity);

        @Mapping(source = "siakTahunKurikulum.tahun", target = "tahunKurikulum")
        @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "programStudi")
        public abstract MataKuliahResDto mataKuliahDto(MataKuliah entity);
        public abstract List<MataKuliahResDto> mataKuliahDtoList(List<MataKuliah> entityList);


        public abstract List<JadwalKuliah> jadwalKuliahList(List<JadwalKuliah> entityList);

        @AfterMapping
        protected void afterMapping(@MappingTarget KrsResDto dto, KrsRincianMahasiswa entity) {
            jadwalKuliahHelper.mapJadwalKuliahToDto(dto, entity);
        }
    }
