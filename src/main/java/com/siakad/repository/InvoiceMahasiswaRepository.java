package com.siakad.repository;

import com.siakad.entity.InvoiceMahasiswa;
import com.siakad.entity.Mahasiswa;
import com.siakad.entity.PeriodeAkademik;
import org.springframework.data.jpa.repository.EntityGraph;
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

    List<InvoiceMahasiswa> findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarIsNull(UUID siakMahasiswaId);

    @EntityGraph(attributePaths = {
            "invoicePembayaranKomponenMahasiswaList",
            "invoicePembayaranKomponenMahasiswaList.invoiceKomponen"
    })
    List<InvoiceMahasiswa> findAllBySiakMahasiswa_IdAndIsDeletedFalseAndTanggalBayarNotNull(UUID siakMahasiswaId);

    List<InvoiceMahasiswa> findAllByTanggalBayarIsNotNullAndIsDeletedFalse();

    Optional<InvoiceMahasiswa> findFirstBySiakMahasiswa_IdAndIsDeletedFalseOrderByCreatedAtDesc(UUID mahasiswaId);

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

    List<InvoiceMahasiswa> findAllBySiakMahasiswa_IdAndIsDeletedFalseOrderByCreatedAtDesc(UUID mahasiswaId);

    @Query(value = """
        select
            SUM(total_tagihan) as total_tagihan,
            SUM(total_bayar) as total_bayar,
            (SUM(total_tagihan) - SUM(total_bayar)) as sisaTagihan,
            MIN(tanggal_tenggat) as tanggal_tenggat
            from siak_invoice_mahasiswa
        where siak_mahasiswa_id = :id
    """, nativeQuery = true)
    Optional<Object[]> findInfoTagihanByMahasiswaId(@Param("id") UUID id);


    @Query(value = """
        select
            im.kode_invoice,
            im.metode_bayar,
            pa.nama_periode,
            im.tanggal_tenggat,
            im.tanggal_bayar,
            ikm.kode_komponen,
            ikm.nama,
            ipk.tagihan,
            im.status
        from siak_invoice_pembayaran_komponen as ipk
            inner join siak_invoice_mahasiswa as im on ipk.siak_invoice_mahasiswa_id=im.id
            inner join siak_invoice_komponen_mahasiswa as ikm on ipk.siak_invoice_komponen_mahasiswa_id=ikm.id
            inner join siak_periode_akademik as pa on im.siak_periode_akademik_id = pa.id
        where im.siak_mahasiswa_id = :mahasiswaId
            and (:status IS NULL OR :status = '' OR lower(im.status) = lower(:status))
    
            and (:namaPeriode IS NULL OR :namaPeriode = '' OR lower(pa.nama_periode) = lower(:namaPeriode))
    
            and (:keyword IS NULL OR :keyword = '' OR
                    lower(ikm.nama) like lower(concat('%', :keyword, '%')) OR\s
                    lower(ikm.kode_komponen) like lower(concat('%', :keyword, '%')) OR\s
                    lower(im.kode_invoice) like lower(concat('%', :keyword, '%'))
                )
        """, nativeQuery = true)
    List<Object[]> findAllTagihanByMahasiswaId(
            @Param("mahasiswaId") UUID mahasiswaId,
            @Param("status") String status,
            @Param("namaPeriode") String namaPeriode,
            @Param("keyword") String keyword
    );
}
