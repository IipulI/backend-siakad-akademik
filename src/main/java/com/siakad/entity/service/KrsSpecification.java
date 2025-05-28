package com.siakad.entity.service;

import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

public class KrsSpecification extends QuerySpecification<KrsRincianMahasiswa> {

    private Specification<KrsRincianMahasiswa> byKelas(String param) {
        return attributeContains("siakKelasKuliah.nama", param);
    }

    private Specification<KrsRincianMahasiswa> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<KrsRincianMahasiswa> entitySearch(String keyword) {
        Specification<KrsRincianMahasiswa> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(
                    Specification.where(byKelas(keyword))
            );
        }

        return spec;
    }
}
