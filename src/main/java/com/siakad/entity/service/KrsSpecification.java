package com.siakad.entity.service;

import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;
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

//    private Specification<KrsRincianMahasiswa> byIdMahasiswa(UUID idMahasiswa) {
//        return (root, query, cb) -> {
//            if (idMahasiswa == null) return null;
//            return cb.equal(
//                    root.get("siakKrsMahasiswa").get("siakMahasiswa").get("id"),
//                    idMahasiswa
//            );
//        };
//    }

    public Specification<KrsRincianMahasiswa> entitySearch(String keyword, List<String> semesters, UUID idMahasiswa) {
        Specification<KrsRincianMahasiswa> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(byMataKuliah(keyword))
                    .or(byKodeMataKuliah(keyword));
        }

//        if (idMahasiswa != null) {
//            spec = spec.and(byIdMahasiswa(idMahasiswa));
//        }

        if (semesters != null && !semesters.isEmpty()){
            spec = spec.and(bySemesterIn(semesters));
        }

        return spec;
    }

    public Specification<KrsRincianMahasiswa> entitySearchKelas(String keyword) {
        Specification<KrsRincianMahasiswa> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(byMataKuliah(keyword));
        }


        return spec;
    }
}
