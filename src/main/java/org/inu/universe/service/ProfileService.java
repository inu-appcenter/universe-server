package org.inu.universe.service;

import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.model.hashtag.HashTagSaveRequest;
import org.inu.universe.model.profile.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    ProfileResponse saveProfile(Long accountId, ProfileSaveRequest request);

    List<HashTagResponse> findHashTag(HashTagSaveRequest request);

    ProfileResponse updateProfile(Long accountId, MultipartFile image, ProfileUpdateRequest request);

    ProfileResponse findProfile(Long profileId);
}
