package com.siakad.repository;

import com.siakad.entity.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, UUID> {
    Optional<Dosen> findByIdAndIsDeletedFalse(UUID id);

    @Query("""
        SELECT p FROM Dosen p WHERE p.id IN :ids AND p.isDeleted=false
    """)
    List<Dosen> findAllByIdAndIsDeletedFalse(@Param("ids") List<UUID> ids);
}
