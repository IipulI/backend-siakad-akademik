package com.siakad.repository;

import com.siakad.entity.Role;
import com.siakad.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(RoleType name);

    @Query(value = """
    SELECT r.*
    FROM roles r
    JOIN user_role ur ON r.id = ur.role_id
    JOIN users u ON ur.user_id = u.id
    WHERE u.id = :id
    """, nativeQuery = true)
    List<Role> findByUserId(@Param("id") UUID id);


}
