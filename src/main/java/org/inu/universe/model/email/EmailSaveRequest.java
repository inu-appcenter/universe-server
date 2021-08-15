package org.inu.universe.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSaveRequest {

    @NotBlank(message = "이메일 주소(필수)")
    private String address;

    @NotBlank(message = "이메일 인증 번호(필수)")
    private String authKey;
}
