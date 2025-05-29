package com.siakad.entity.service;

import com.siakad.entity.CapaianMataKuliah;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class CapaianMataKuliahSpecification extends QuerySpecification<CapaianMataKuliah> {

    private Specification<CapaianMataKuliah> byIdMataKuliah(UUID param){
        return attributeEqual("siakMataKuliah.id", param);
    }

    private Specification<CapaianMataKuliah> byIsDeleted(){
        return attributeEqual("isDeleted", false);
    }

    public Specification<CapaianMataKuliah> entitySearch(UUID idMataKuliah){
        Specification<CapaianMataKuliah> spec = byIsDeleted();
        spec = spec.and(byIdMataKuliah(idMataKuliah));

        return spec;
    }

}
