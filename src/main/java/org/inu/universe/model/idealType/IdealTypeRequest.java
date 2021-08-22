package org.inu.universe.model.idealType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdealTypeRequest {

    @NotBlank(message = "지역(필수)")
    private String region;

    @NotBlank(message = "성별(필수")
    private String gender;

    @NotNull(message = "나이(필수)")
    private Integer age;

    @NotBlank(message = "단과대(필수)")
    private String college;
}
