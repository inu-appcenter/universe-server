package org.inu.universe.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.Account;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private String accountId;

    private String profileId;

    private String idealTypeId;

    public static AccountResponse from(String accountId, String profileId, String idealTypeId) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.accountId = accountId;
        accountResponse.profileId = profileId;
        accountResponse.idealTypeId = idealTypeId;
        return accountResponse;
    }

    public static AccountResponse from(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.accountId = account.getId().toString();
        accountResponse.profileId = account.getProfile().getId().toString();
        accountResponse.idealTypeId = account.getIdealType().getId().toString();
        return accountResponse;
    }
}
