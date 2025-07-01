package com.siakad.entity.service;

import com.siakad.entity.KrsRincianMahasiswa;
import com.siakad.util.QuerySpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class KrsSpecification extends QuerySpecification<KrsRincianMahasiswa> {

    private Specification<KrsRincianMahasiswa> byMataKuliah(String param) {
        return attributeContains("siakKelasKuliah.siakMataKuliah.namaMataKuliah", param);
    }

    private Specification<KrsRincianMahasiswa> byIsDeleted() {
        return attributeEqual("isDeleted", false);
    }

    private Specification<KrsRincianMahasiswa> byIdMahasiswa(UUID idMahasiswa) {
        return (root, query, cb) -> {
            if (idMahasiswa == null) return null;
            return cb.equal(
                    root.get("siakKrsMahasiswa").get("siakMahasiswa").get("id"),
                    idMahasiswa
            );
        };
    }

    public Specification<KrsRincianMahasiswa> entitySearch(String keyword, UUID idMahasiswa) {
        Specification<KrsRincianMahasiswa> spec = byIsDeleted();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and(byMataKuliah(keyword));
        }

        if (idMahasiswa != null) {
            spec = spec.and(byIdMahasiswa(idMahasiswa));
        }

        return spec;
    }
}
