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

    Optional<Role> findByNama(RoleType nama);

    @Query(value = """
    SELECT r.*
    FROM siak_role r
    JOIN siak_user_role ur ON r.id = ur.siak_role_id
    JOIN siak_user u ON ur.siak_user_id = u.id
    WHERE u.id = :id
    """, nativeQuery = true)
    List<Role> findByUserId(@Param("id") UUID id);


}
