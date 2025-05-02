package com.siakad.util;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.domain.Specification;

public abstract class QuerySpecification<T> {

    // String LIKE (case-insensitive)
    protected Specification<T> attributeContains(String attributeName, String value) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get(attributeName)), "%" + value.toLowerCase() + "%");
    }

    // Boolean EQUAL
    protected Specification<T> attributeEqualBoolean(String attributeName, boolean value) {
        return (root, query, cb) ->
                cb.equal(root.get(attributeName), value);
    }

    // Exact match (case-sensitive by default)
    protected Specification<T> attributeEqual(String attributeName, Object value) {
        return (root, query, cb) ->
                cb.equal(root.get(attributeName), value);
    }

    // Attribute IS NULL
    protected Specification<T> attributeIsNull(String attributeName) {
        return (root, query, cb) -> cb.isNull(root.get(attributeName));
    }

    // Attribute IS NOT NULL
    protected Specification<T> attributeIsNotNull(String attributeName) {
        return (root, query, cb) -> cb.isNotNull(root.get(attributeName));
    }
}
