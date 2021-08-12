package org.inu.universe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class ProfileImage {

    private String imageStoreName;
    private String profileImageUrl;
    private String thumbnailImageUrl;

    public static ProfileImage saveImage(String imageStoreName, String profileImageUrl, String thumbnailImageUrl) {
        ProfileImage profileImage = new ProfileImage();
        profileImage.imageStoreName = imageStoreName;
        profileImage.profileImageUrl = profileImageUrl;
        profileImage.thumbnailImageUrl = thumbnailImageUrl;
        return profileImage;
    }
}
