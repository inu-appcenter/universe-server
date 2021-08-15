package org.inu.universe.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {

    @Test
    @DisplayName("이메일 저장")
    void saveEmail() {
        Email email = Email.saveEmail("a@inu.ac.kr");

        assertThat(email.getAddress()).isEqualTo("a@inu.ac.kr");
    }

    @Test
    @DisplayName("Admin 이메일 저장")
    void saveAdminEmail() {
        Email email = Email.saveAdminEmail("a@inu.ac.kr");

        assertThat(email.getAddress()).isEqualTo("a@inu.ac.kr");
    }

}