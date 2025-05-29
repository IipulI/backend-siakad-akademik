package com.siakad.entity.service;

import com.siakad.entity.SkalaPenilaian;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class SkalaPenilaianSpecification extends QuerySpecification<SkalaPenilaian> {

    private Specification<SkalaPenilaian> byTahunAjaran(String param) {
        return attributeContains("siakTahunAjaran.tahun", param);
    }

    private Specification<SkalaPenilaian> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<SkalaPenilaian> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<SkalaPenilaian> entitySearch(String tahunAjaran, String programStudi) {
        Specification<SkalaPenilaian> spec = byIsDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if (!Strings.isBlank(tahunAjaran)){
            spec = spec.and(byTahunAjaran(tahunAjaran));
        }

        return spec;
    }
}
