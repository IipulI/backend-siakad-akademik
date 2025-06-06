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

    private Specification<PembimbingAkademik> byProgramStudi(String param) {
        return attributeEqual("siakMahasiswa.siakP  rogramStudi.namaProgramStudi", param);
    }

    private Specification<PembimbingAkademik> bySemester(Integer param) {
        return attributeEqual("siakMahasiswa.semester", param);
    }

    private Specification<PembimbingAkademik> siakDosenIsNotNull() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNotNull(root.get("siakDosen"));
    }



}
