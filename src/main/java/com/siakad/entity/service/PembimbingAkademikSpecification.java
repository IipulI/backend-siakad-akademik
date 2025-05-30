package com.siakad.entity.service;

import com.siakad.entity.PembimbingAkademik;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

public class PembimbingAkademikSpecification extends QuerySpecification<PembimbingAkademik> {

    private Specification<PembimbingAkademik> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    private Specification<PembimbingAkademik> byPeriodeAkademik(String param) {
        return attributeEqual("siakPeriodeAkademik.namaPeriode", param);
    }

//    private Specification<PembimbingAkademik>
}
