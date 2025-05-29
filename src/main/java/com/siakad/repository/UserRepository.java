package com.siakad.repository;

import com.siakad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    @Query(value = """
        SELECT * FROM siak_user
        WHERE username = :usernameOrEmail
        OR email = :usernameOrEmail
    """, nativeQuery = true)
    Optional<User> findByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);

    Optional<User> findByIdAndIsDeletedFalse(UUID id)   ;
}
