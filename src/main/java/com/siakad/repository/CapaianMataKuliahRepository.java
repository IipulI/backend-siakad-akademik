package com.siakad.repository;

import com.siakad.entity.CapaianMataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CapaianMataKuliahRepository extends JpaRepository<CapaianMataKuliah, UUID>, JpaSpecificationExecutor<CapaianMataKuliah> {
    Optional<CapaianMataKuliah> findByIdAndIsDeletedFalse(UUID id);
}
