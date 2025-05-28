package com.siakad.repository;

import com.siakad.entity.Ruangan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RuanganRepository extends JpaRepository<Ruangan, UUID> {

    Optional<Ruangan> findByIdAndIsDeletedFalse(UUID id);

    List<Ruangan> findAllByIsDeletedFalse();
}
