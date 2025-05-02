package com.siakad.repository;

import com.siakad.entity.InvoiceMahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvoiceMahasiswaRepository extends JpaRepository<InvoiceMahasiswa, UUID> {
}
