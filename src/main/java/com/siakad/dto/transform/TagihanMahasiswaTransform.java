package com.siakad.dto.transform;

import com.siakad.dto.response.RingkasanTagihanResDto;
import com.siakad.dto.response.RingkasanTagihanSourceDto;
import com.siakad.dto.response.TagihanMahasiswaResDto;
import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagihanMahasiswaTransform {

    @Mapping(target = "id", source = "invoiceMahasiswa.id")
    @Mapping(target = "tanggal", source = "invoiceMahasiswa.createdAt", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "kodeTagihan", source = "invoiceMahasiswa.kodeInvoice")
    @Mapping(target = "npm", source = "invoiceMahasiswa.siakMahasiswa.npm")
    @Mapping(target = "nama", source = "invoiceMahasiswa.siakMahasiswa.nama")
    @Mapping(target = "jenisTagihan", source = "invoiceKomponen.nama")
    @Mapping(target = "nominal", source = "invoiceMahasiswa.totalTagihan")
    @Mapping(target = "bayar", source = "invoiceMahasiswa.totalBayar")
    @Mapping(target = "tanggalBayar", source = "invoiceMahasiswa.tanggalBayar")
    @Mapping(target = "tanggalTenggat", source = "invoiceMahasiswa.tanggalTenggat")
    @Mapping(
            target = "lunas",
            expression = "java(entity.getTagihan() != null && entity.getTagihan().compareTo(java.math.BigDecimal.ZERO) == 0)"
    )
    @Mapping(target = "semester", source = "invoiceMahasiswa.siakMahasiswa.semester")
    @Mapping(target = "angkatan", source = "invoiceMahasiswa.siakMahasiswa.angkatan")
    @Mapping(target = "programStudi", source = "invoiceMahasiswa.siakMahasiswa.siakProgramStudi.namaProgramStudi")
    @Mapping(target = "periodeAkademik", source = "invoiceMahasiswa.siakPeriodeAkademik.namaPeriode")
    @Mapping(target = "fakultas", source = "invoiceMahasiswa.siakMahasiswa.siakProgramStudi.siakFakultas.namaFakultas")
    @Mapping(target = "kelasKuliah", source = "rincianKrsMahasiswa.siakKelasKuliah.nama")
    TagihanMahasiswaResDto toDto(InvoicePembayaranKomponenMahasiswa entity);

    List<TagihanMahasiswaResDto> toDtoList(List<InvoicePembayaranKomponenMahasiswa> entities);

    @Mapping(target = "totalBelumBayar", expression = "java(source.getTotalTagihan().subtract(source.getTotalTerbayar()))")
    RingkasanTagihanResDto toDto(RingkasanTagihanSourceDto source);
}

