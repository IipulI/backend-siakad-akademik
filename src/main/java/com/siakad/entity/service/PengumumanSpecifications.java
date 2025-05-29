package com.siakad.entity.service;

import com.siakad.entity.Pengumuman;
import com.siakad.util.QuerySpecification;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@Mapper(componentModel = "spring")
public class PengumumanSpecifications extends QuerySpecification<Pengumuman> {

    // --- Constants for status parameters ---
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PRIORITY = "PRIORITY";

    // --- Private Specific Filter Methods ---

    /**
     * Filters for Pengumuman where isDeleted is false.
     */
    private Specification<Pengumuman> notDeleted() {
        // Assumes attributeEqual is provided by QuerySpecification<Pengumuman>
        return attributeEqual("isDeleted", false);
    }

    /**
     * Filters for Pengumuman where isActive is true.
     */
    private Specification<Pengumuman> byIsActive() {
        return attributeEqual("isActive", true);
    }

    /**
     * Filters for Pengumuman where isActive is false.
     */
    private Specification<Pengumuman> byIsNotActive() {
        return attributeEqual("isActive", false);
    }

    /**
     * Filters for Pengumuman where isPriority is true.
     */
    private Specification<Pengumuman> byIsPriority() {
        return attributeEqual("isPriority", true);
    }

    /**
     * Filters for Pengumuman where the 'judul' (title) contains the given keyword.
     * Case-insensitive search.
     */
    private Specification<Pengumuman> byJudulContains(String keyword) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("judul")), "%" + keyword.toLowerCase() + "%");
    }

    // --- Public Entity Search Method ---

    /**
     * Builds a Specification for Pengumuman based on optional search criteria.
     *
     * @param keyword      Optional keyword to search in the 'judul' (title).
     * @param status       Optional status to filter by. Valid values are "ACTIVE", "INACTIVE", "PRIORITY".
     * Other values will be ignored or could throw an exception.
     * @param userId       Optional UUID of the siakUser.
     * @return A Specification<Pengumuman> based on the provided criteria.
     */
    public Specification<Pengumuman> entitySearch(String keyword, String status, java.util.UUID userId) {
        Specification<Pengumuman> spec = notDeleted();

        // Filter by status
        if (StringUtils.hasText(status)) {
            switch (status.toUpperCase()) {
                case STATUS_ACTIVE:
                    spec = spec.and(byIsActive());
                    break;
                case STATUS_INACTIVE:
                    spec = spec.and(byIsNotActive());
                    break;
                case STATUS_PRIORITY:
                    spec = spec.and(byIsPriority());
                    break;
            }
        }

        if (StringUtils.hasText(keyword)) {
            spec = spec.and(byJudulContains(keyword));
        }

        if (userId != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("siakUser").get("id"), userId)
            );
        }

        return spec;
    }
}