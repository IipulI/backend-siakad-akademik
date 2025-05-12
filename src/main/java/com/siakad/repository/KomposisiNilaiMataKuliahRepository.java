package com.siakad.repository;

import com.siakad.entity.KomposisiNilaiMataKuliah;
import com.siakad.entity.KomposisiNilaiMataKuliahId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KomposisiNilaiMataKuliahRepository extends JpaRepository<KomposisiNilaiMataKuliah, KomposisiNilaiMataKuliahId> {
    List<KomposisiNilaiMataKuliah> findAllBySiakMataKuliah_IdAndIsDeletedFalse(UUID siakMataKuliahId);
}
