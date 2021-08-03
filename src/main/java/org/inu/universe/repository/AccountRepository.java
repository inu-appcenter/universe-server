package org.inu.universe.repository;

import org.inu.universe.domain.Account;
import org.inu.universe.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(Email email);
}
