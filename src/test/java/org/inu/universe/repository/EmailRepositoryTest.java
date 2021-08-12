package org.inu.universe.repository;

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
class EmailRepositoryTest {

    @Autowired
    private EmailRepository emailRepository;

    @Test
    @DisplayName("이메일 저장")
    public void save() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        Email savedEmail = emailRepository.save(email);

        assertNotNull(savedEmail.getId());
    }

    @Test
    @DisplayName("이메일 조회 by Id")
    public void findById() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        Email savedEmail = emailRepository.save(email);

        Email findEmail = emailRepository.findById(savedEmail.getId()).get();

        assertThat(findEmail.getId()).isEqualTo(savedEmail.getId());
    }

    @Test
    @DisplayName("이메일 조회 by Address")
    public void findByAddress() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        Email savedEmail = emailRepository.save(email);

        Email findEmail = emailRepository.findByAddress("a@inu.ac.kr").get();

        assertThat(findEmail.getId()).isEqualTo(savedEmail.getId());
    }
}