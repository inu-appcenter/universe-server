package org.inu.universe.service;


import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.token.TokenDto;

import java.util.List;

public interface AccountService {

    void saveAccount(AccountSaveRequest request);

    TokenDto loginAccount(AccountLoginRequest request);

    String reissue(String refreshToken);

    void deleteAccountByAdmin(Long accountId);

    AccountResponse findId(Long accountId);
}
