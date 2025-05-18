package com.siakad.entity.service;

import com.siakad.entity.Rps;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class RpsSpecification extends QuerySpecification<Rps> {

    private Specification<Rps> byTahunKurikulum(String param) {
        return attributeContains("siakTahunKurikulum.tahun", param);
    }

    private Specification<Rps> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<Rps> byPeriodeAkademik(String param) {
        return attributeContains("siakPeriodeAkdemik.namaPeriodeAkademik", param);
    }

    private Specification<Rps> byHasKelas(Boolean hasKelas) {
        return (root, query, cb) -> {
            if (Boolean.TRUE.equals(hasKelas)) {
                return cb.isNotEmpty(root.get("kelasKuliahList"));
            } else {
                return cb.isEmpty(root.get("kelasKuliahList"));
            }
        };
    }


    private Specification<Rps> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<Rps> entitySearch(String tahunKurikulum,
                                           String programStudi,
                                           String periodeAkademik,
                                           Boolean hasKelas
                                           ) {

        Specification<Rps> spec = byIsDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        if (!Strings.isBlank(periodeAkademik)){
            spec = spec.and(byPeriodeAkademik(periodeAkademik));
        }

        if (hasKelas != null) {
            spec = spec.and(byHasKelas(hasKelas));
        }

        return spec;
    }
}
