package com.siakad.entity.service;

import com.siakad.entity.Mahasiswa;
import com.siakad.util.QuerySpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class MahasiswaSpecification extends QuerySpecification<Mahasiswa> {

    private Specification<Mahasiswa> byIsDeletedFalse() {
        return attributeEqualBoolean("isDeleted", false);
    }

    private Specification<Mahasiswa> byNama(String param) {
        return attributeContains("nama", param.trim());
    }

    private Specification<Mahasiswa> byJurusan(String param) {
        return attributeContains("jurusan", param.trim());
    }

    private Specification<Mahasiswa> byId(String param) {
        try {
            UUID uuid = UUID.fromString(param.trim());
            return (root, query, cb) -> cb.equal(root.get("id"), uuid);
        } catch (IllegalArgumentException e) {
            return (root, query, cb) -> cb.disjunction(); // Return false condition if invalid UUID
        }
    }

    public Specification<Mahasiswa> byEntitySearch(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return byIsDeletedFalse();
        }

        Specification<Mahasiswa> combinedSpec = byNama(keyword)
                .or(byJurusan(keyword));

        try {
            UUID.fromString(keyword.trim());
            combinedSpec = combinedSpec.or(byId(keyword));
        } catch (IllegalArgumentException ignored) {}

        return byIsDeletedFalse().and(combinedSpec);
    }
}
