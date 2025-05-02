package com.siakad.repository;

import com.siakad.entity.ProgramStudi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProgramStudiRepository extends JpaRepository<ProgramStudi, UUID> {
    Optional<ProgramStudi> findByIdAndIsDeletedFalse(UUID id);
}
