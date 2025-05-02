package com.siakad.util;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public abstract class QuerySpecification<T> {

    protected Specification<T> attributeEqual(String attribute, Object value) {
        return (root, query, cb) -> cb.equal(getPath(root, attribute), value);
    }

    protected Specification<T> attributeEqualString(String attribute, String value) {
        return (root, query, cb) -> cb.equal(cb.lower(getPath(root, attribute)), value.toLowerCase());
    }

    protected Specification<T> attributeContains(String attribute, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, attribute)), "%" + value.toLowerCase() + "%");
    }

    protected Specification<T> attributeStartsWith(String attribute, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(getPath(root, attribute)), value.toLowerCase() + "%");
    }

    protected Specification<T> attributeDateEqual(String attribute, String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            return (root, query, cb) -> cb.equal(getPath(root, attribute), date);
        } catch (DateTimeParseException e) {
            return (root, query, cb) -> cb.conjunction(); // no-op
        }
    }

    @SuppressWarnings("unchecked")
    private <Y> Path<Y> getPath(Root<T> root, String attribute) {
        if (attribute.contains(".")) {
            String[] parts = attribute.split("\\.");
            Path<?> path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                path = path.get(parts[i]);
            }
            return (Path<Y>) path;
        }
        return root.get(attribute);
    }
}
