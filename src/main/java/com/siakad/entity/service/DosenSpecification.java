package com.siakad.entity.service;

import com.siakad.entity.Dosen;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

public class DosenSpecification extends QuerySpecification<Dosen> {

    private Specification<Dosen> byNama(String param){
        return attributeContains("nama", param);
    }

    private Specification<Dosen> byNidn(String param){
        return attributeContains("nidn", param);
    }

    private Specification<Dosen> byIsDeleted(){
        return attributeEqual("isDeleted", false);
    }

    public Specification<Dosen> entitySearch(String keyword){
        Specification<Dosen> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(
                    Specification.where(byNama(keyword))
                            .or(byNidn(keyword))
            );
        }

        return spec;
    }


}
