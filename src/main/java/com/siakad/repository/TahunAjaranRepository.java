package com.siakad.repository;

import com.siakad.entity.TahunAjaran;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TahunAjaranRepository extends JpaRepository<TahunAjaran, UUID>,
        JpaSpecificationExecutor<TahunAjaran> {
    Optional<TahunAjaran> findByIdAndIsDeletedFalse(UUID id);
    Page<TahunAjaran> findAllByIsDeletedFalse(Specification<TahunAjaran> spec, Pageable pageable);
}
