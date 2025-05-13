package com.siakad.entity.service;

import com.siakad.entity.ProfilLulusan;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class ProfilLulusanSpecification extends QuerySpecification<ProfilLulusan> {

    private Specification<ProfilLulusan> byTahunKurikulum(String param){
        return attributeContains("siakTahunKurikulum.tahun", param);
    }

    private Specification<ProfilLulusan> byIsDeleted(){
        return attributeEqual("isDeleted", false);
    }

    public Specification<ProfilLulusan> entitySearch(String tahunKurikulum){
        Specification<ProfilLulusan> spec = byIsDeleted();

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        return spec;
    }

}
