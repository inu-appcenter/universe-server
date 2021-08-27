package org.inu.universe.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.inu.universe.domain.Profile;
import org.inu.universe.domain.ProfileImage;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckProfile {   // 비교 조건들 + 추천 프로필 기본 정보

    private Long id;

    private ProfileImage profileImage;

    private String nickName;

    private Integer age;

    private String gender;

    private String region;

    private String major;

    private List<String> hashTagList;

    private double similarity;

    public static CheckProfile from(Profile profile) {
        CheckProfile checkProfile = new CheckProfile();
        checkProfile.id = profile.getId();
        checkProfile.profileImage = profile.getProfileImage();
        checkProfile.nickName = profile.getNickName();
        checkProfile.age = profile.getAge();
        checkProfile.gender = profile.getGender();
        checkProfile.region = profile.getRegion();
        checkProfile.major = profile.getMajor();
        checkProfile.hashTagList = profile.getProfileTagList().stream().map(profileTag -> profileTag.getHashTag().getName()).collect(Collectors.toList());
        return checkProfile;
    }
}
