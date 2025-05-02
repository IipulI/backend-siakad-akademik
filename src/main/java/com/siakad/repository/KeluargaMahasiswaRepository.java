package com.siakad.repository;

import com.siakad.entity.KeluargaMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KeluargaMahasiswaRepository extends JpaRepository<KeluargaMahasiswa, UUID> {
    Optional<KeluargaMahasiswa> findByIdAndIsDeletedFalse(UUID id);
}
