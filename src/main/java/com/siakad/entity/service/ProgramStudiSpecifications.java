package com.siakad.entity.service;

import com.siakad.entity.ProgramStudi;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProgramStudiSpecifications {

    public static Specification<ProgramStudi> withFilters(UUID tahunKurikulumId, UUID programStudiId, UUID jenjangId) {
        return (root, query, cb) -> {
            Predicate predicate = cb.isFalse(root.get("isDeleted"));

            if (programStudiId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("id"), programStudiId));
            }

            if (jenjangId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("siakJenjang").get("id"), jenjangId));
            }

            if (tahunKurikulumId != null) {
                // Karena tahunKurikulum relasi ke ProfilLulusan/CPL, ini akan dicari di service
            }

            return predicate;
        };
    }
}
