package org.inu.universe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.status.AccountRole;
import org.inu.universe.domain.status.AccountStatus;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email email;

    private String password;

    private String refreshToken;        // accessToken 만료 시, refreshToken을 이용하여 재발급

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    public static Account saveAccount(Email email, String password) {
        Account account = new Account();
        account.email = email;
        account.password = password;
        account.status = AccountStatus.ACTIVE;
        account.role = AccountRole.ROLE_USER;
        return account;
    }

    public static Account saveAdminAccount(Email email, String password) {
        Account account = new Account();
        account.email = email;
        account.password = password;
        account.status = AccountStatus.ACTIVE;
        account.role = AccountRole.ROLE_ADMIN;
        return account;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
