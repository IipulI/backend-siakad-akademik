package com.siakad.repository;

import com.siakad.entity.InvoicePembayaranKomponenMahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoicePembayaranKomponenMahasiswaRepository extends JpaRepository<InvoicePembayaranKomponenMahasiswa, UUID>,
        JpaSpecificationExecutor<InvoicePembayaranKomponenMahasiswa>
{

    Page<InvoicePembayaranKomponenMahasiswa> findByIsDeletedFalse(Pageable pageable);
    List<InvoicePembayaranKomponenMahasiswa> findAllByIsDeletedFalse();
    Optional<InvoicePembayaranKomponenMahasiswa> findByInvoiceMahasiswa_IdAndIsDeletedFalse(UUID Id);
}
