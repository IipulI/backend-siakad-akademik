package com.siakad.repository;

import com.siakad.entity.BatasSks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatasSksRepository extends JpaRepository<BatasSks, UUID> {

    Optional<BatasSks> findByIdAndIsDeletedFalse(UUID id);
    Optional<BatasSks> findAllByIsDeletedFalse();
}
