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

    private String password;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private AccountRole role;

    @OneToOne(fetch = FetchType.LAZY)    // 주 테이블에 외래 키. 단방향
    @JoinColumn(name = "email_id")
    private Email email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

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

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
