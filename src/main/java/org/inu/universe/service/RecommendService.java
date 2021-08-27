package org.inu.universe.service;

import org.inu.universe.model.profile.ProfileDto;

import java.util.List;

public interface RecommendService {
    List<ProfileDto> findRecommendProfile(Long accountId);

    void dislikeProfile(Long accountId, Long profileId);

    void recommendProfile();
}
