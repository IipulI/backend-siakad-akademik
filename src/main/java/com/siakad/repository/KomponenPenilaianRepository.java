package com.siakad.repository;

import com.siakad.entity.KomponenPenilaian;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KomponenPenilaianRepository extends JpaRepository<KomponenPenilaian, UUID> {

    @Query(value = """
        SELECT * FROM siak_komponen_penilaian m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<KomponenPenilaian> findAllNotDeleted(Pageable pageable);

    Optional<KomponenPenilaian> findByIdAndIsDeletedFalse(UUID id);
}
