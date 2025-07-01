package com.siakad.repository;

import com.siakad.entity.Jenjang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JenjangRepository extends JpaRepository<Jenjang, UUID> {
    List<Jenjang> findAllByIsDeletedFalse();

    Optional<Jenjang> findByIdAndIsDeletedFalse(UUID id);
}
