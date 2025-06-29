package com.siakad.repository;

import com.siakad.entity.BatasSks;
import com.siakad.entity.Jenjang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BatasSksRepository extends JpaRepository<BatasSks, UUID> {

    Optional<BatasSks> findByIdAndIsDeletedFalse(UUID id);
    List<BatasSks> findAllByIsDeletedFalse();

    Optional<BatasSks> findFirstBySiakJenjangAndIpsMinLessThanEqualAndIpsMaxGreaterThanEqualAndIsDeletedFalse(
            Jenjang siakJenjang, BigDecimal ipsMin, BigDecimal ipsMax
    );

    List<BatasSks> findBySiakJenjangId(UUID jenjangId);

//    @Query("SELECT b FROM BatasSks b WHERE b.siakJenjang = :jenjang AND :ips >= b.ipsMin AND :ips < b.ipsMax")
//    Optional<BatasSks> findBatasSksByJenjangAndIps(@Param("jenjang") Jenjang jenjang, @Param("ips") BigDecimal ips);
}
