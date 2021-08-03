package org.inu.universe.domain;

import org.inu.universe.domain.status.EmailStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    @DisplayName("이메일 저장")
    void saveEmail() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        assertThat(email.getAddress()).isEqualTo("a@inu.ac.kr");
        assertThat(email.getStatus()).isEqualTo(EmailStatus.NOTAUTH);
    }

    @Test
    @DisplayName("Admin 이메일 저장")
    void saveAdminEmail() {
        Email email = Email.saveAdminEmail("a@inu.ac.kr");

        assertThat(email.getAddress()).isEqualTo("a@inu.ac.kr");
        assertThat(email.getStatus()).isEqualTo(EmailStatus.AUTH);
    }

    @Test
    @DisplayName("이메일 인증")
    void authEmail() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        email.authEmail();

        assertThat(email.getStatus()).isEqualTo(EmailStatus.AUTH);
    }
}