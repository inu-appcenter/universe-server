package org.inu.universe.domain;

import org.inu.universe.domain.status.AccountRole;
import org.inu.universe.domain.status.AccountStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    @DisplayName("계정 생성")
    void saveAccount() {
        Email email = Email.saveEmail("a@inu.ac.kr");
        Account account = Account.saveAccount(email, "a");

        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getPassword()).isEqualTo("a");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(account.getRole()).isEqualTo(AccountRole.ROLE_USER);
    }

    @Test
    @DisplayName("관리자 계정 생성")
    void saveAdminAccount() {
        Email email = Email.saveEmail("a@inu.ac.kr");
        Account account = Account.saveAdminAccount(email, "a");

        assertThat(account.getEmail()).isEqualTo(email);
        assertThat(account.getPassword()).isEqualTo("a");
        assertThat(account.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(account.getRole()).isEqualTo(AccountRole.ROLE_ADMIN);
    }

    @Test
    @DisplayName("계정 RefreshToken 설정")
    void setRefreshToken() {
        Email email = Email.saveEmail("a@inu.ac.kr");
        Account account = Account.saveAdminAccount(email, "a");

        account.setRefreshToken("refreshToken");

        assertThat(account.getRefreshToken()).isEqualTo("refreshToken");
    }
}