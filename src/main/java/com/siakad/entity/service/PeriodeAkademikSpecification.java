package com.siakad.entity.service;

import com.siakad.entity.PeriodeAkademik;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class PeriodeAkademikSpecification extends QuerySpecification<PeriodeAkademik> {

    private Specification<PeriodeAkademik> byTahun(String tahun) {
        return attributeContains("siakTahunAjaran.tahun", tahun);
    }

    private Specification<PeriodeAkademik> byNamaPeriode(String namaPeriode) {
        return attributeContains("namaPeriode", namaPeriode);
    }

    private Specification<PeriodeAkademik> byKodePeriode(String kodePeriode) {
        return attributeContains("kodePeriode", kodePeriode);
    }

    private Specification<PeriodeAkademik> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<PeriodeAkademik> entitySearch(String keyword) {
        Specification<PeriodeAkademik> spec = notDeleted();

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byKodePeriode(keyword))
                            .or(byNamaPeriode(keyword))
            );
        }

        return spec;
    }
}
