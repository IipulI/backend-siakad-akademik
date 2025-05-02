package com.siakad.repository;

import com.siakad.entity.Fakultas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FakultasRepository extends JpaRepository<Fakultas, UUID> {
}
