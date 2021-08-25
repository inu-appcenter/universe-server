package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    private Long id;

    private String thumbnailImageUrl;

    private String nickName;

    private Integer age;

    private String gender;

    private String major;

    public static ProfileDto from(Profile profile) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.id = profile.getId();
        profileDto.thumbnailImageUrl = profile.getProfileImage().getThumbnailImageUrl();
        profileDto.nickName = profile.getNickName();
        profileDto.age = profile.getAge();
        profileDto.gender = profile.getGender();
        profileDto.major = profile.getMajor();
        return profileDto;
    }
}
