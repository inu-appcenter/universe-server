package org.inu.universe.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthRequest {

    @NotBlank(message = "이메일 인증 번호(필수)")
    private String authKey;
}
