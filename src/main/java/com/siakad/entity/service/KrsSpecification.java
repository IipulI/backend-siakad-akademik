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
}

//// Your KrsSpecification (modified)
//package com.siakad.entity.service; // Or wherever KrsSpecification resides
//
//import com.siakad.entity.KrsRincianMahasiswa;
//import com.siakad.util.QuerySpecification; // Your base class
//import org.springframework.data.jpa.domain.Specification;
//
//import java.util.List;
//import java.util.UUID;
//
//public class KrsSpecification extends QuerySpecification<KrsRincianMahasiswa> {
//
//    private Specification<KrsRincianMahasiswa> byMataKuliah(String param) {
//        return attributeContains("siakKelasKuliah.siakMataKuliah.namaMataKuliah", param);
//    }
//
//    private Specification<KrsRincianMahasiswa> byKodeMataKuliah(String param) {
//        return attributeContains("siakKelasKuliah.siakMataKuliah.kodeMataKuliah", param);
//    }
//
//    private Specification<KrsRincianMahasiswa> bySemesterIn(List<Integer> semesters) { // Changed to List<Integer>
//        return (root, query, builder) -> {
//            if (semesters == null || semesters.isEmpty()) {
//                return builder.conjunction();
//            }
//            // Ensure mk.semester is Integer
//            return root.get("siakKelasKuliah").get("siakMataKuliah").get("semester").in(semesters);
//        };
//    }
//
//    private Specification<KrsRincianMahasiswa> byIsDeleted() {
//        return attributeEqual("isDeleted", false);
//    }
//
//    // This method will be used for the EXPLICIT filter for the current student's KRS
//    private Specification<KrsRincianMahasiswa> byIdMahasiswa(UUID idMahasiswa) {
//        return (root, query, cb) -> {
//            if (idMahasiswa == null) return cb.conjunction(); // Return true (no filter) if null
//            return cb.equal(
//                    root.get("siakKrsMahasiswa").get("siakMahasiswa").get("id"),
//                    idMahasiswa
//            );
//        };
//    }
//
//    // This is the new method for general KrsRincianMahasiswa search without student ID filter
//    public Specification<KrsRincianMahasiswa> generalKrsSearch(String keyword, List<Integer> semesters) { // Changed to List<Integer>
//        Specification<KrsRincianMahasiswa> spec = byIsDeleted();
//
//        if (keyword != null && !keyword.isEmpty()) {
//            spec = spec.and(byMataKuliah(keyword)
//                    .or(byKodeMataKuliah(keyword)));
//        }
//
//        if (semesters != null && !semesters.isEmpty()){
//            spec = spec.and(bySemesterIn(semesters));
//        }
//
//        return spec;
//    }
//
//    // You can keep the original entitySearch if it's used elsewhere for student-specific queries
//    public Specification<KrsRincianMahasiswa> entitySearch(String keyword, List<Integer> semesters, UUID idMahasiswa) { // Changed to List<Integer>
//        Specification<KrsRincianMahasiswa> spec = byIsDeleted();
//
//        if (keyword != null && !keyword.isEmpty()) {
//            spec = spec.and(byMataKuliah(keyword))
//                    .or(byKodeMataKuliah(keyword));
//        }
//
//        if (idMahasiswa != null) {
//            spec = spec.and(byIdMahasiswa(idMahasiswa));
//        }
//
//        if (semesters != null && !semesters.isEmpty()){
//            spec = spec.and(bySemesterIn(semesters));
//        }
//
//        return spec;
//    }
//}