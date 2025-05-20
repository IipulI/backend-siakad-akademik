package com.siakad.entity.service;

import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class InvoicePembayaranSpecification extends QuerySpecification<InvoicePembayaranKomponenMahasiswa> {

    private Specification<InvoicePembayaranKomponenMahasiswa> byIsDeleted(){
        return attributeEqual("isDeleted", false);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byNpm(String param) {
        return  attributeContains("invoiceMahasiswa.siakMahasiswa.npm", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byNama(String param) {
        return  attributeContains("invoiceMahasiswa.siakMahasiswa.nama", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> bySemester(Integer param) {
        return attributeEqual("invoiceMahasiswa.siakMahasiswa.semester", param);
    }


    private Specification<InvoicePembayaranKomponenMahasiswa> byAngkatan(String param) {
        return  attributeContains("invoiceMahasiswa.siakMahasiswa.angkatan", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byProgramStudi(String param) {
        return  attributeContains("invoiceMahasiswa.siakMahasiswa.siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byPeriodeAkademik(String param) {
        return  attributeContains("invoiceMahasiswa.siakPeriodeAkademik.namaPeriode", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byFakultas(String param) {
        return  attributeContains("invoiceMahasiswa.siakMahasiswa.siakProgramStudi.siakFakultas.namaFakultas", param);
    }

    private Specification<InvoicePembayaranKomponenMahasiswa> byKelasKuliah(String param) {
        return  attributeContains("rincianKrsMahasiswa.siakKelasKuliah.nama", param);
    }

    public Specification<InvoicePembayaranKomponenMahasiswa> entitySearch(String keyword, String npm, String nama, Integer semester, String angkatan, String programStudi, String fakultas, String periodeAkademik){
        Specification<InvoicePembayaranKomponenMahasiswa> spec = byIsDeleted();

        if (!Strings.isBlank(npm)){
            spec = spec.and(byNpm(npm));
        }

        if (!Strings.isBlank(nama)){
            spec = spec.and(byNama(nama));
        }

        if (!Strings.isBlank(periodeAkademik)) {
            spec = spec.and(byPeriodeAkademik(periodeAkademik));
        }

        if (semester != null) {
            spec = spec.and(bySemester(semester));
        }

        if (!Strings.isBlank(angkatan)){
            spec = spec.and(byAngkatan(angkatan));
        }

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(fakultas)){
            spec = spec.and(byFakultas(fakultas));
        }

        if (!Strings.isBlank(keyword)){
            spec = spec.and(
                    Specification.where(byKelasKuliah(keyword))
            );
        }

        return spec;
    }
}
