package org.inu.universe;

import org.inu.universe.domain.Account;
import org.inu.universe.domain.Email;
import org.inu.universe.domain.status.AccountRole;
import org.inu.universe.domain.status.AccountStatus;
import org.inu.universe.domain.status.EmailStatus;
import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.email.EmailAuthRequest;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.token.TokenDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public class TestFixture {

    public static final AccountSaveRequest ACCOUNT_SAVE_REQUEST
            = new AccountSaveRequest("a@inu.ac.kr", "a", "a");

    public static final AccountLoginRequest ACCOUNT_LOGIN_REQUEST
            = new AccountLoginRequest("a@inu.ac.kr", "a");

    public static final EmailSaveRequest EMAIL_SAVE_REQUEST
            = new EmailSaveRequest("a@inu.ac.kr");

    public static final EmailAuthRequest EMAIL_AUTH_REQUEST
            = new EmailAuthRequest("123456");

    public static final Email EMAIL
            = new Email(1L, "a@inu.ac.kr", EmailStatus.AUTH);

    public static final Email EMAIL_2
            = new Email(1L, "a@inu.ac.kr", EmailStatus.NOTAUTH);

    public static final Account ACCOUNT
            = new Account(1L, EMAIL, "a", null, AccountStatus.ACTIVE, AccountRole.ROLE_USER);

    public static final Account ACCOUNT_2
            = new Account(1L, EMAIL, "a", "abcd", AccountStatus.ACTIVE, AccountRole.ROLE_USER);

    public static final TokenDto TOKEN_DTO
            = new TokenDto("abcdefg", "ABCDEFG");

    public static final String REISSUE_ACCESS_TOKEN ="hijklmn";

}
