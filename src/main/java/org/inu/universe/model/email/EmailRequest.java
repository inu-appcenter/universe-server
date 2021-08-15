package org.inu.universe.model.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    @Email
    @NotBlank(message = "학교 이메일(필수)")
    private String address;
}
