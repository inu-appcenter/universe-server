package org.inu.universe.repository;

import org.inu.universe.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findByAddress(String address);
}
