package com.siakad.entity.service;

import com.siakad.entity.TahunAjaran;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class TahunAjaranSpecification extends QuerySpecification<TahunAjaran> {
    private Specification<TahunAjaran> byTahun(String tahun) {
        return attributeContains("tahun", tahun);
    }

    private Specification<TahunAjaran> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<TahunAjaran> entitySearch(String keyword) {
        Specification<TahunAjaran> spec = notDeleted();

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byTahun(keyword))
            );
        }

        return spec;
    }
}
