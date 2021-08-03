package org.inu.universe.model.account;

import lombok.Data;
import org.inu.universe.domain.Account;
import org.inu.universe.domain.Email;
import org.inu.universe.domain.status.AccountRole;
import org.inu.universe.domain.status.AccountStatus;

@Data
public class AccountResponse {

    private Long id;
    private Email email;
    private String password;
    private String refreshToken;
    private AccountRole role;
    private AccountStatus status;

    public static AccountResponse from(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.id = account.getId();
        accountResponse.email = account.getEmail();
        accountResponse.password = account.getPassword();
        accountResponse.refreshToken = account.getRefreshToken();
        accountResponse.role = account.getRole();
        accountResponse.status = account.getStatus();
        return accountResponse;
    }

}
