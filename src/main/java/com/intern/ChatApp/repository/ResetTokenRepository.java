package com.intern.ChatApp.repository;

import com.intern.ChatApp.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Integer> {
    Optional<ResetToken> findByToken(String token);
    void deleteByToken(String token);
}
