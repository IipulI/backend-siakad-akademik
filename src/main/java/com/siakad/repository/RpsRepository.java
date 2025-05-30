package com.siakad.repository;

import com.siakad.entity.Rps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RpsRepository extends JpaRepository<Rps, UUID>,
        JpaSpecificationExecutor<Rps> {
    Optional<Rps> findByIdAndIsDeletedFalse(UUID id);
}
