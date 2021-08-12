package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSaveRequest {

    @NotBlank(message = "닉네임(필수)")
    private String nickName;

    @NotNull(message = "나이(필수)")
    private Integer age;

    @NotBlank(message = "성별(필수)")
    private String gender;

    @NotBlank(message = "학과(필수)")
    private String major;
}
