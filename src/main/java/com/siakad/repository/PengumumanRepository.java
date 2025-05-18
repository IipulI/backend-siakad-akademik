package com.siakad.repository;

import com.siakad.entity.Pengumuman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PengumumanRepository extends JpaRepository<Pengumuman, UUID>,
        JpaSpecificationExecutor<Pengumuman> {

    Optional<Pengumuman> findByIdAndIsDeletedFalse(UUID id);
}
