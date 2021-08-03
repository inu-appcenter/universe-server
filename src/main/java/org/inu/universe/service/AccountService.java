package org.inu.universe.service;


import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.token.TokenDto;

public interface AccountService {

    AccountResponse saveAccount(AccountSaveRequest request);

    TokenDto loginAccount(AccountLoginRequest request);

    String reissue(Long accountId, String refreshToken);
}
