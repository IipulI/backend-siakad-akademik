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

    @Mapping(target = "tanggal", source = "invoiceMahasiswa.createdAt", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "kodeTagihan", source = "invoiceMahasiswa.kodeInvoice")
    @Mapping(target = "npm", source = "invoiceMahasiswa.siakMahasiswa.npm")
    @Mapping(target = "nama", source = "invoiceMahasiswa.siakMahasiswa.nama")
    @Mapping(target = "jenisTagihan", source = "invoiceKomponen.nama")
    @Mapping(target = "nominal", source = "invoiceKomponen.nominal")
    @Mapping(target = "bayar", source = "tagihan") // asumsi ini jumlah yang dibayar
    @Mapping(target = "lunas", expression = "java(entity.getTagihan().compareTo(entity.getInvoiceKomponen().getNominal()) == 0)")
    TagihanMahasiswaResDto toDto(InvoicePembayaranKomponenMahasiswa entity);

    List<TagihanMahasiswaResDto> toDtoList(List<InvoicePembayaranKomponenMahasiswa> entities);


    @Mapping(target = "totalBelumBayar", expression = "java(source.getTotalTagihan().subtract(source.getTotalTerbayar()))")
    RingkasanTagihanResDto toDto(RingkasanTagihanSourceDto source);
}

