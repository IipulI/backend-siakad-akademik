package com.siakad.entity.service;

import com.siakad.entity.CapaianPembelajaranLulusan;
import com.siakad.util.QuerySpecification;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

public class CapaianPembelajaranLulusanSpecification extends QuerySpecification<CapaianPembelajaranLulusan> {

    private Specification<CapaianPembelajaranLulusan> byTahunKurikulum(String param){
        return attributeContains("siakTahunKurikulum.tahun", param);
    }

    private Specification<CapaianPembelajaranLulusan> byIsDeleted(){
        return attributeEqual("isDeleted", false);
    }

    public Specification<CapaianPembelajaranLulusan> entitySearch(String tahunKurikulum){
        Specification<CapaianPembelajaranLulusan> spec = byIsDeleted();

        if (!Strings.isBlank(tahunKurikulum)){
            spec = spec.and(byTahunKurikulum(tahunKurikulum));
        }

        return spec;
    }

}
