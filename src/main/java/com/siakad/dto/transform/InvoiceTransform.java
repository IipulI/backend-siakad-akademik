package com.siakad.dto.transform;

import com.siakad.dto.request.InvoiceKomponenMahasiswaReqDto;
import com.siakad.dto.request.InvoiceKomponenReqDto;
import com.siakad.dto.request.InvoiceMahasiswaReqDto;
import com.siakad.dto.response.InvoiceKomponenResDto;
import com.siakad.dto.response.InvoiceMahasiswaResDto;
import com.siakad.dto.response.MahasiswaKeuanganResDto;
import com.siakad.entity.InvoiceKomponen;
import com.siakad.entity.InvoiceMahasiswa;
import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import com.siakad.entity.Mahasiswa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface InvoiceTransform {

    @Mapping(source = "siakMahasiswaId", target = "siakMahasiswa.id")
    InvoiceMahasiswa toEntity(InvoiceMahasiswaReqDto requestDto);

    @Mapping(source = "komponenId", target = "id")
    InvoiceKomponen toKomponenEntity(InvoiceKomponenReqDto komponenDto);

    @Mapping(source = "siakMahasiswa.id", target = "siakMahasiswaId")
    @Mapping(source = "invoicePembayaranKomponenMahasiswaList", target = "komponen", qualifiedByName = "toKomponenResList")
    InvoiceMahasiswaResDto toResDto(InvoiceMahasiswa inv);

    @Named("toKomponenResList")
    default List<InvoiceKomponenResDto> toKomponenResList(List<InvoicePembayaranKomponenMahasiswa> list) {
        return list.stream().map(this::toKomponen).collect(Collectors.toList());
    }

    @Mapping(source = "invoiceKomponen.kodeKomponen", target = "kodeKomponen")
    @Mapping(source = "invoiceKomponen.nama", target = "nama")
    @Mapping(source = "tagihan", target = "nominal")
    InvoiceKomponenResDto toKomponen(InvoicePembayaranKomponenMahasiswa pem);

    InvoiceKomponen toEntity(InvoiceKomponenMahasiswaReqDto requestDto);
    InvoiceKomponenResDto toDto(InvoiceKomponen entity);
    void toEntity(InvoiceKomponenMahasiswaReqDto requestDto, @MappingTarget InvoiceKomponen entity);

    @Mapping(source = "npm", target = "npm")
    @Mapping(source = "nama", target = "nama")
    @Mapping(source = "siakProgramStudi.namaProgramStudi", target = "namaProgramStudi")
    @Mapping(source = "siakProgramStudi.siakFakultas.namaFakultas", target = "namaFakultas")
    @Mapping(target = "semester", ignore = true) // karena tidak tersedia di entity
    MahasiswaKeuanganResDto toKeuanganDto(Mahasiswa mahasiswa);

    List<MahasiswaKeuanganResDto> toKeuanganDtoList(List<Mahasiswa> mahasiswaList);


    default List<InvoicePembayaranKomponenMahasiswa> toPembayaranList(
            List<InvoiceKomponenReqDto> dtoList,
            InvoiceMahasiswa invoice) {
        if (dtoList == null) return new ArrayList<>();

        return dtoList.stream().map(dto -> {
            InvoicePembayaranKomponenMahasiswa ent = new InvoicePembayaranKomponenMahasiswa();
            ent.setId(UUID.randomUUID());
            ent.setInvoiceMahasiswa(invoice);

            InvoiceKomponen komponen = new InvoiceKomponen();
            komponen.setId(dto.getKomponenId());        // ← note getKomponenId()
            ent.setInvoiceKomponen(komponen);            // ← note setInvoiceKomponen(...)

            ent.setTagihan(dto.getTagihan());
            ent.setIsDeleted(false);
            ent.setCreatedAt(java.time.LocalDateTime.now());
            return ent;
        }).collect(Collectors.toList());
    }

}
