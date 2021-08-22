package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.HashTag;
import org.inu.universe.domain.Profile;
import org.inu.universe.domain.ProfileImage;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {

    private Long id;

    private ProfileImage profileImage;

    private String nickName;

    private Integer age;

    private String gender;

    private String college;

    private String major;

    private String region;

    private String height;

    private String bodyType;

    private String mbti;

    private String introduction;

    private List<String> hashTagList;

    private boolean profilePrivate;

    public static ProfileResponse from(Profile profile) {
        ProfileResponse profileResponse = new ProfileResponse();
        profileResponse.id = profile.getId();
        profileResponse.profileImage = profile.getProfileImage();
        profileResponse.nickName = profile.getNickName();
        profileResponse.age = profile.getAge();
        profileResponse.gender = profile.getGender();
        profileResponse.college = profile.getCollege();
        profileResponse.major = profile.getMajor();
        profileResponse.region = profile.getRegion();
        profileResponse.height = profile.getHeight();
        profileResponse.bodyType = profile.getBodyType();
        profileResponse.mbti = profile.getMbti();
        profileResponse.introduction = profile.getIntroduction();
        profileResponse.hashTagList = profile.getProfileTagList().stream().map(tag -> tag.getHashTag().getName()).collect(Collectors.toList());
        profileResponse.profilePrivate = profile.isProfilePrivate();
        return  profileResponse;
    }

}
