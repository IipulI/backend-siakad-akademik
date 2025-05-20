package com.siakad.repository;

import com.siakad.entity.Fakultas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FakultasRepository extends JpaRepository<Fakultas, UUID> {
    Optional<Fakultas> findByIdAndIsDeletedFalse(UUID id);
    List<Fakultas> findAllByIsDeletedFalse();
}
