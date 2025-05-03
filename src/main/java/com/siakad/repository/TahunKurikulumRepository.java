package com.siakad.repository;

import com.siakad.entity.TahunKurikulum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TahunKurikulumRepository extends JpaRepository<TahunKurikulum, UUID>,
        JpaSpecificationExecutor<TahunKurikulum> {
    Optional<TahunKurikulum> findByIdAndIsDeletedFalse(UUID uuid);
}
