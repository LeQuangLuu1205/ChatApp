package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Page<User> findAllByIsDisabledFalse(Pageable pageable);
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
            "FROM User u " +
            "WHERE u.email = :email AND u.role.roleName = 'ADMIN'")
    boolean existsByEmailAndIsAdmin(@Param("email") String email);
}
