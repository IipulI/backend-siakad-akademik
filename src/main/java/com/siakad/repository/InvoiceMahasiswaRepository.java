package com.siakad.repository;

import com.siakad.entity.InvoiceMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.entity.PeriodeAkademik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceMahasiswaRepository extends JpaRepository<InvoiceMahasiswa, UUID> {

    Optional<InvoiceMahasiswa> findByIdAndIsDeletedFalse(UUID id);

    Optional<InvoiceMahasiswa> findBySiakMahasiswa_IdAndIsDeletedFalse(UUID siakMahasiswaId);

    List<InvoiceMahasiswa> findAllByTanggalBayarIsNotNullAndIsDeletedFalse();

    @Query("SELECT COALESCE(SUM(im.totalTagihan), 0) FROM InvoiceMahasiswa im WHERE im.isDeleted = false")
    BigDecimal sumTotalTagihanAktif();

    @Query("SELECT COALESCE(SUM(im.totalTagihan), 0) " +
            "FROM InvoiceMahasiswa im " +
            "JOIN im.siakMahasiswa m " +
            "JOIN m.siakProgramStudi ps " +
            "JOIN ps.siakFakultas f " +
            "WHERE f.id = :fakultasId " +
            "AND im.isDeleted = false " +
            "AND m.isDeleted = false " +
            "AND ps.isDeleted = false")
    BigDecimal sumTotalTagihanByFakultas(@Param("fakultasId") UUID fakultasId);

    List<InvoiceMahasiswa> findBySiakPeriodeAkademikAndSiakMahasiswa(PeriodeAkademik siakPeriodeAkademik, Mahasiswa siakMahasiswa);

}
