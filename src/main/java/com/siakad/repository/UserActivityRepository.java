package com.siakad.repository;

import com.siakad.entity.SiakUserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<SiakUserActivity, UUID> {
}
