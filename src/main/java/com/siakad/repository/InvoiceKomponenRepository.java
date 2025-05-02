package com.siakad.repository;

import com.siakad.entity.InvoiceKomponen;
import com.siakad.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceKomponenRepository extends JpaRepository<InvoiceKomponen, UUID> {

    @Query(value = """
        SELECT * FROM siak_invoice_komponen m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<InvoiceKomponen> findAllNotDeleted(Pageable pageable);

    Optional<InvoiceKomponen> findByIdAndIsDeletedFalse(UUID id);
}
