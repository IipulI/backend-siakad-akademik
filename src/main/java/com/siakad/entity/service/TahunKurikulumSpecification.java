package com.siakad.entity.service;

import com.siakad.entity.TahunKurikulum;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class TahunKurikulumSpecification extends QuerySpecification<TahunKurikulum> {
    private Specification<TahunKurikulum> byTahun(String tahun) {
        return attributeContains("tahun", tahun);
    }

    private Specification<TahunKurikulum> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<TahunKurikulum> entitySearch(String keyword) {
        Specification<TahunKurikulum> spec = notDeleted();

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byTahun(keyword))
            );
        }

        return spec;
    }

}
