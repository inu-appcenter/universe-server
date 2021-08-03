package org.inu.universe.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountSaveRequest {

    @Email
    @NotBlank(message = "학교 이메일(필수)")          // null, "", " " 허용X
    private String address;

    @NotBlank(message = "비밀번호(필수)")
    private String password;

    @NotBlank(message = "비밀번호 재확인(필수)")
    private String password2;
}
