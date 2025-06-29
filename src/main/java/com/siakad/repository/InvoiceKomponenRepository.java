package com.siakad.repository;

import com.siakad.entity.InvoiceKomponen;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceKomponenRepository extends JpaRepository<InvoiceKomponen, UUID> {

    @Query(value = """
        SELECT * FROM siak_invoice_komponen_mahasiswa m WHERE m.is_deleted = false
            AND (m.kode_komponen ilike '%' || :keyword || '%' OR m.nama ilike '%' || :keyword || '%')
    """, nativeQuery = true)
    Page<InvoiceKomponen> findAllNotDeleted(@Param("keyword") String keyword, Pageable pageable);

    Optional<InvoiceKomponen> findByIdAndIsDeletedFalse(UUID id);

    @Query(value = """
        SELECT * FROM siak_invoice_komponen_mahasiswa m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<InvoiceKomponen> findAllAndIsDeletedIsFalse(Pageable pageable);
}
