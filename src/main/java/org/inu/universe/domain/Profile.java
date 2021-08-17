package org.inu.universe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Profile extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Embedded
    private ProfileImage profileImage;

    @Column(nullable = false, unique = true)
    private String nickName;

    private Integer age;

    private String gender;

    private String college;

    private String major;

    private boolean militaryStatus;

    private boolean graduationStatus;

    private String region;

    private String height;

    private String bodyType;

    private String mbti;

    private String introduction;

    private boolean profilePrivate;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileTag> profileTagList = new ArrayList<>();

    public static Profile saveProfile(String nickName, Integer age, String gender, String college, String major) {
        Profile profile = new Profile();
        profile.nickName = nickName;
        profile.age = age;
        profile.gender = gender;
        profile.college = college;
        profile.major = major;
        return profile;
    }

    public static Profile setPrivate(Long id, String nickName, Integer age, String gender, String college, String major) {
        Profile profile = new Profile();
        profile.id = id;
        profile.nickName = nickName;
        profile.age = age;
        profile.gender = gender;
        profile.college = college;
        profile.major = major;
        return profile;
    }

    public void updateProfile(ProfileImage image, String nickName, Integer age, String gender, String college, String major, boolean militaryStatus, boolean graduationStatus, String region, String height, String bodyType, String mbti, String introduction, boolean profilePrivate) {
        this.profileImage = image;
        this.nickName = nickName;
        this.age = age;
        this.gender = gender;
        this.college = college;
        this.major = major;
        this.militaryStatus = militaryStatus;
        this.graduationStatus = graduationStatus;
        this.region = region;
        this.height = height;
        this.bodyType = bodyType;
        this.mbti = mbti;
        this.introduction = introduction;
        this.profilePrivate = profilePrivate;
    }
}
