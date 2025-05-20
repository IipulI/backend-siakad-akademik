package com.siakad.entity.service;

import com.siakad.entity.Mahasiswa;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class MahasiswaSpecification extends QuerySpecification<Mahasiswa> {

    private Specification<Mahasiswa> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }

    private Specification<Mahasiswa> byNpm(String param) {
        return attributeContains("npm", param);
    }

    private Specification<Mahasiswa> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    private Specification<Mahasiswa> byFakultas(String param) {
        return attributeContains("siakProgramStudi.siakFakultas.namaFakultas", param);
    }

    private Specification<Mahasiswa> byAngkatan(String param) {
        return attributeContains("angkatan", param);
    }

    private Specification<Mahasiswa> bySemester(Integer param) {
        return attributeEqual("semester", param);
    }

    private Specification<Mahasiswa> byNama(String param) {
        return attributeContains("nama", param);
    }

    public Specification<Mahasiswa> entitySearch(String keyword, String fakultas, String angkatan, Integer semester, String programStudi, String npm) {
        Specification<Mahasiswa> spec = byIsDeleted();

        if (!Strings.isBlank(programStudi)){
            spec = spec.and(byProgramStudi(programStudi));
        }

        if(!Strings.isBlank(fakultas)){
            spec = spec.and(byFakultas(fakultas));
        }

        if(!Strings.isBlank(angkatan)){
            spec = spec.and(byAngkatan(angkatan));
        }

        if (semester != null) {
            spec = spec.and(bySemester(semester));
        }

        if (!Strings.isBlank(npm)){
            spec = spec.and(byNpm(npm));
        }

        if(!Strings.isBlank(keyword)){
            spec = spec.and(
                    Specification.where(byNama(keyword))
            );
        }

        return spec;
    }

}
