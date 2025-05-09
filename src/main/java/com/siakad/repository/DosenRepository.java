package com.siakad.repository;

import com.siakad.entity.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, UUID> {
    Optional<Dosen> findByIdAndIsDeletedFalse(UUID id);
}
