package com.siakad.entity.service;

import com.siakad.entity.Mahasiswa;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

public class MahasiswaSpecification extends QuerySpecification<Mahasiswa> {

    private Specification<Mahasiswa> byProgramStudi(String param) {
        return attributeContains("siakProgramStudi.namaProgramStudi", param);
    }


}
