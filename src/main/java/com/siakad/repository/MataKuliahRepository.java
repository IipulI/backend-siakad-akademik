package com.siakad.repository;

import com.siakad.entity.MataKuliah;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MataKuliahRepository extends JpaRepository<MataKuliah, UUID>,
        JpaSpecificationExecutor<MataKuliah> {
    Optional<MataKuliah> findByIdAndIsDeletedFalse(UUID id);
}
