package com.siakad.entity.service;

import com.siakad.entity.ProgramStudi;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProgramStudiSpecification {
    public static Specification<ProgramStudi> build(String tahunKurikulum, String namaProdi, String namaJenjang) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            root.fetch("siakJenjang"); // Eager fetch untuk performa

            if (StringUtils.hasText(tahunKurikulum)) {
                // Filter prodi yang memiliki mata kuliah pada tahun kurikulum tertentu
                predicates.add(criteriaBuilder.equal(root.join("mataKuliahList").join("siakTahunKurikulum").get("tahun"), tahunKurikulum));
            }

            if (StringUtils.hasText(namaProdi)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("namaProgramStudi")), "%" + namaProdi.toLowerCase() + "%"));
            }

            if (StringUtils.hasText(namaJenjang)) {
                predicates.add(criteriaBuilder.equal(root.get("siakJenjang").get("jenjang"), namaJenjang));
            }

            query.distinct(true);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}