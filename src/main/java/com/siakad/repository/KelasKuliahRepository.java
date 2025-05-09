package com.siakad.repository;

import com.siakad.entity.KelasKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KelasKuliahRepository extends JpaRepository<KelasKuliah, UUID>, JpaSpecificationExecutor<KelasKuliah> {
    Optional<KelasKuliah> findByIdAndIsDeletedFalse(UUID id);
}
