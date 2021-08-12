package org.inu.universe.repository;

import org.inu.universe.domain.Account;
import org.inu.universe.domain.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    @DisplayName("계정 생성")
    public void save() {

        Email email = Email.saveEmail("a@inu.ac.kr");
        email.authEmail();
        Account account = Account.saveAccount(email, "a");

        Account savedAccount = accountRepository.save(account);

        assertNotNull(savedAccount.getId());
    }

    @Test
    @DisplayName("계정 조회 by Id")
    public void findById() {
        Email email = Email.saveEmail("a@inu.ac.kr");
        email.authEmail();
        Account account = Account.saveAccount(email, "a");

        Account savedAccount = accountRepository.save(account);

        Account findAccount = accountRepository.findById(savedAccount.getId()).get();

        assertThat(findAccount.getId()).isEqualTo(savedAccount.getId());
    }

    @Test
    @DisplayName("계정 조회 by Email")
    public void findByEmail() {
        Email email = Email.saveEmail("a@inu.ac.kr");
        email.authEmail();
        Email savedEmail = emailRepository.save(email);
        Account account = Account.saveAccount(email, "a");

        Account savedAccount = accountRepository.save(account);
        Account findAccount = accountRepository.findByEmail(savedEmail).get();

        assertThat(findAccount.getId()).isEqualTo(savedAccount.getId());
    }
}