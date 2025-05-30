package com.siakad.repository;

import com.siakad.entity.BatasSks;
import com.siakad.entity.Jenjang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatasSksRepository extends JpaRepository<BatasSks, UUID> {

    Optional<BatasSks> findByIdAndIsDeletedFalse(UUID id);
    Optional<BatasSks> findAllByIsDeletedFalse();

    Optional<BatasSks> findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(
            Jenjang siakJenjang, BigDecimal ipsMin, BigDecimal ipsMax
    );
}
