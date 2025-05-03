package com.siakad.entity.service;

import com.siakad.entity.MataKuliah;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class MataKuliahSpecification extends QuerySpecification<MataKuliah> {

    private Specification<MataKuliah> byTahunKurikulum(String tahunKurikulum) {
        return attributeContains("siakTahunKurikulum.tahun", tahunKurikulum);
    }

    private Specification<MataKuliah> byProgramStudi(String programStudi) {
        return attributeContains("siakProgramStudi.namaProgramStudi", programStudi);
    }

    private Specification<MataKuliah> byJenisMataKuliah(String jenisMataKuliah) {
        return attributeContains("jenisMataKuliah", jenisMataKuliah);
    }

    private Specification<MataKuliah> byMataKuliah(String namaMataKuliah) {
        return attributeContains("namaMataKuliah", namaMataKuliah);
    }

    private Specification<MataKuliah> notDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<MataKuliah> entitySearch(String keyword,
                                                  String programStudi,
                                                  String jenisMataKuliah,
                                                  String tahunKurikulum
                                                  ) {
        Specification<MataKuliah> spec = notDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        if (!Strings.isBlank(jenisMataKuliah)){
            spec = spec.and(byJenisMataKuliah(jenisMataKuliah));
        }

        if (!Strings.isBlank(keyword)) {
            spec = spec.and(
                    Specification.where(byMataKuliah(keyword))
            );
        }

        return spec;
    }
}
