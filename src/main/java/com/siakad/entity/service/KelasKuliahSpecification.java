package com.siakad.entity.service;

import com.siakad.entity.KelasKuliah;
import com.siakad.entity.MataKuliah;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class KelasKuliahSpecification extends QuerySpecification<KelasKuliah> {

    private Specification<KelasKuliah> byPeriodeAkademik(String param) {
        return attributeContains("siakPeriodeAkademik.namaPeriodeAkademik", param);
    }

    private Specification<KelasKuliah> byKurikulum(String param) {
        return attributeContains("siakMataKuliah.siakTahunKurikulum.tahun", param);
    }

    private Specification<KelasKuliah> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<KelasKuliah> bySistemKuliah(String param) {
        return attributeContains("sistemKuliah", param);
    }

    private Specification<KelasKuliah> byNama(String param) {
        return attributeContains("nama", param);
    }

    private Specification<KelasKuliah> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public  Specification<KelasKuliah> entitySearch(String keyword,
                                                    String periodeAkademik,
                                                    String tahunKuriKulum,
                                                    String programStudi,
                                                    String sistemKuliah
                                                    ){
        Specification<KelasKuliah> spec = notDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(periodeAkademik)){
            spec = spec.and(byPeriodeAkademik(periodeAkademik));
        }

        if (!Strings.isBlank(sistemKuliah)){
            spec = spec.and(bySistemKuliah(sistemKuliah));
        }

        if (!Strings.isBlank(tahunKuriKulum)){
            spec = spec.and(byKurikulum(tahunKuriKulum));
        }

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byNama(keyword))
            );
        }

        return spec;
    }
}
