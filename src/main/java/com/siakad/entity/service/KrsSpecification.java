package com.siakad.entity.service;

import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class KrsSpecification extends QuerySpecification<KrsRincianMahasiswa> {

    private Specification<KrsRincianMahasiswa> byMataKuliah(String param) {
        return attributeContains("siakKelasKuliah.siakMataKuliah.namaMataKuliah", param);
    }

    private Specification<KrsRincianMahasiswa> byKodeMataKuliah(String param) {
        return attributeContains("siakKelasKuliah.siakMataKuliah.kodeMataKuliah", param);
    }

    private Specification<KrsRincianMahasiswa> bySemesterIn(List<String> semesters) {
        return (root, query, builder) -> {
            if (semesters == null || semesters.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("siakKelasKuliah").get("siakMataKuliah").get("semester").in(semesters);
        };
    }

    private Specification<KrsRincianMahasiswa> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    public Specification<KrsRincianMahasiswa> entitySearch(String keyword, List<String> semesters) {
        Specification<KrsRincianMahasiswa> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(
                    Specification.where(byMataKuliah(keyword))
                            .or(byKodeMataKuliah(keyword))
            );
        }

        if (semesters != null && !semesters.isEmpty()){
            spec = spec.and(bySemesterIn(semesters));
        }

        return spec;
    }
}
