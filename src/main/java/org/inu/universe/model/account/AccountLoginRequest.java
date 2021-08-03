package org.inu.universe.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginRequest {

    @Email
    @NotBlank(message = "학교 이메일(필수)")
    private String address;

    @NotBlank(message = "비밀번호(필수)")
    private String password;
}
