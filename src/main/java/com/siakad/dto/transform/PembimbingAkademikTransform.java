    package com.siakad.dto.transform;

    import com.siakad.dto.request.PembimbingAkademikReqDto;
    import com.siakad.dto.response.PembimbingAkademikResDto;
    import com.siakad.entity.PembimbingAkademik;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.MappingTarget;

    import java.math.BigDecimal;

    @Mapper(componentModel = "spring")
    public interface PembimbingAkademikTransform {
        PembimbingAkademik toEntity(PembimbingAkademikReqDto dto);

        @Mapping(source = "siakMahasiswa.nama", target = "mahasiswa")
        @Mapping(source = "siakMahasiswa.angkatan", target = "angkatan")
        @Mapping(source = "siakMahasiswa.statusMahasiswa", target = "statusMahasiswa")
        @Mapping(source = "siakMahasiswa.semester", target = "semester")
        @Mapping(source = "siakMahasiswa.siakProgramStudi.namaProgramStudi", target = "programStudi")
        @Mapping(source = "siakPeriodeAkademik.namaPeriode", target = "periodeAkademik")
        PembimbingAkademikResDto toDto(PembimbingAkademik entity);

        void toEntity(PembimbingAkademikReqDto dto, @MappingTarget PembimbingAkademik entity);

        default PembimbingAkademikResDto toFullDto(
                PembimbingAkademik entity,
                Integer batasSks,
                Integer totalSks,
                BigDecimal ips,
                BigDecimal ipk,
                Boolean statusDiajukan,
                Boolean statusDisetujui,
                String pembimbingAkademik
        ) {
            PembimbingAkademikResDto dto = toDto(entity);
            dto.setBatasSks(batasSks);
            dto.setTotalSks(totalSks);
            dto.setIps(ips);
            dto.setIpk(ipk);
            dto.setStatusDiajukan(statusDiajukan);
            dto.setStatusDisetujui(statusDisetujui);
            dto.setPembimbingAkademik(pembimbingAkademik);
            return dto;
        }
    }
