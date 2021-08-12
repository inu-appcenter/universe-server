package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.HashTag;
import org.inu.universe.domain.ProfileTag;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {

    @NotBlank(message = "닉네임(필수)")
    private String nickName;

    @NotNull(message = "나이(필수)")
    private Integer age;

    @NotBlank(message = "성별(필수)")
    private String gender;

    @NotBlank(message = "학과(필수)")
    private String major;

    private boolean militaryStatus;

    private boolean graduationStatus;

    private String region;

    private String height;

    private String bodyType;

    private String mbti;

    private String introduction;

    private List<String> hashTagList;

}
