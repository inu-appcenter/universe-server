package org.inu.universe.model.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AccountUpdateRequest {

    @NotBlank(message = "변경할 비밀번호(필수)")
    private String password;
}
