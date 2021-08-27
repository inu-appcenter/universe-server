package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.Profile;
import org.inu.universe.domain.ProfileImage;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {  // - 추천 프로필 기본 정보

    private Long id;

    private ProfileImage profileImage;

    private String nickName;

    private Integer age;

    private String gender;

    private String major;

    public static ProfileDto from(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.id = profile.getId();
        profileDto.profileImage = profile.getProfileImage();
        profileDto.nickName = profile.getNickName();
        profileDto.age = profile.getAge();
        profileDto.gender = profile.getGender();
        profileDto.major = profile.getMajor();
        return profileDto;
    }
}
