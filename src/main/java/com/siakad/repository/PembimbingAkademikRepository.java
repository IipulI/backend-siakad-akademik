package com.siakad.repository;

import com.siakad.entity.PembimbingAkademik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PembimbingAkademikRepository extends JpaRepository<PembimbingAkademik, UUID> {

}
