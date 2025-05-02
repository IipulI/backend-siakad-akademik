package com.siakad.repository;

import com.siakad.entity.KomponenPenilaian;
import com.siakad.entity.TahunAjaran;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TahunAjaranRepository extends JpaRepository<TahunAjaran, UUID> {

    @Query(value = """
        SELECT * FROM siak_tahun_ajaran m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<TahunAjaran> findAllNotDeleted(Pageable pageable);

    Optional<TahunAjaran> findByIdAndIsDeletedFalse(UUID id);
}
