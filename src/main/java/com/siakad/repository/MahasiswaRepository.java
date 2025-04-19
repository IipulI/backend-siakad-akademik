package com.siakad.repository;

import com.siakad.dto.request.MahasiswaReqDto;
import com.siakad.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, UUID> {

    Optional<Mahasiswa> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByNpm(String npm);
    boolean existsByEmail(String email);
    @Query("SELECT m FROM Mahasiswa m WHERE m.isDeleted = false")
    Page<Mahasiswa> findAllNotDeleted(Pageable pageable);

    Optional<Mahasiswa> findByNpmAndIsDeletedFalse(String npm);
}
