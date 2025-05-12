package com.siakad.repository;

import com.siakad.entity.KomposisiPenilaian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KomposisiPenilaianRepository extends JpaRepository<KomposisiPenilaian, UUID> {
    Optional<KomposisiPenilaian> findByIdAndIsDeletedFalse(UUID id);
    Optional<KomposisiPenilaian> findAllByIsDeletedFalse();
}
