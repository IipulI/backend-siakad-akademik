package com.siakad.repository;

import com.siakad.entity.Mahasiswa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, UUID>,
        JpaSpecificationExecutor<Mahasiswa> {

    Optional<Mahasiswa> findByIdAndIsDeletedFalse(UUID id);
    boolean existsByNpm(String npm);
    boolean existsByEmailPribadi(String email);

    @Query(value = """
        SELECT * FROM siak_mahasiswa m WHERE m.is_deleted = false
    """, nativeQuery = true)
    Page<Mahasiswa> findAllNotDeleted(Pageable pageable);

    @Query(value = """
    SELECT m.* FROM siak_mahasiswa m
    JOIN siak_program_studi ps ON m.siak_program_studi_id = ps.id
    JOIN siak_fakultas f ON ps.siak_fakultas_id = f.id
    WHERE m.is_deleted = false
""", nativeQuery = true)
    Page<Mahasiswa> findByWithRelasiNative(Specification<Mahasiswa> spec,Pageable pageable);

    @Query("""
        SELECT m FROM Mahasiswa m WHERE m.statusMahasiswa = 'Aktif' 
    """)
    List<Mahasiswa> findAllByStatusMahasiswa();
}
