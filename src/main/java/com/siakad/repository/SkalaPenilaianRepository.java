package com.siakad.repository;

import com.siakad.entity.SkalaPenilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SkalaPenilaianRepository extends JpaRepository<SkalaPenilaian, UUID>,
        JpaSpecificationExecutor<SkalaPenilaian> {
    Optional<SkalaPenilaian> findByIdAndIsDeletedFalse(UUID uuid);
}
