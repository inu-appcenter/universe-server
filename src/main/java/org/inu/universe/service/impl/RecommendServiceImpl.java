package org.inu.universe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inu.universe.domain.Account;
import org.inu.universe.domain.IdealType;
import org.inu.universe.domain.Profile;
import org.inu.universe.domain.Recommend;
import org.inu.universe.model.profile.CheckProfile;
import org.inu.universe.model.profile.ProfileDto;
import org.inu.universe.repository.*;
import org.inu.universe.repository.query.ProfileQueryRepository;
import org.inu.universe.repository.query.RecommendQueryRepository;
import org.inu.universe.service.RecommendService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Component
public class RecommendServiceImpl implements RecommendService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final IdealTypeRepository idealTypeRepository;
    private final ProfileQueryRepository profileQueryRepository;
    private final RecommendRepository recommendRepository;
    private final RecommendQueryRepository recommendQueryRepository;
    private final RecommendJdbcRepository recommendJdbcRepository;

    /*
    원하는 상대 추천 프로필 조회
    */
    @Override
    public List<ProfileDto> findRecommendProfile(Long accountId) {
        List<Profile> recommendProfile = recommendQueryRepository.findRecommendProfile(accountId);

        return recommendProfile.stream().map(profile -> ProfileDto.from(profile)).collect(Collectors.toList());
    }

    /*
    "관심 없음" 프로필 삭제
     */
    @Override
    @Transactional
    public void dislikeProfile(Long accountId, Long profileId) {
        recommendQueryRepository.dislikeProfile(accountId, profileId);
    }


    /*
    [매일 자정] 원하는 상대 프로필 추천
     */
    @Override
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void recommendProfile() {

        recommendRepository.deleteAllInBatch();

        List<Account> allAccount = accountRepository.findAll();
        List<CheckProfile> allProfile = profileQueryRepository.findAllProfile().stream().map(profile -> CheckProfile.from(profile)).collect(Collectors.toList());

        List<Recommend> recommendList = new ArrayList<>();
        L:
        for (Account findAccount : allAccount) {
            Profile findProfile;
            try {
                findProfile = profileRepository.findById(findAccount.getProfile().getId()).get();
            } catch (NullPointerException e) {
                continue L;
            }

            List<CheckProfile> checkProfiles = checkSimilProfile(findAccount, findProfile, allProfile);  // 추천된 프로필 리스트
            for (CheckProfile profile : checkProfiles) {
                recommendList.add(Recommend.saveRecommend(findAccount, profile.getId()));
            }
        }
        recommendJdbcRepository.batchUpdate(recommendList);
    }

    // - 유사도 확인
    private List<CheckProfile> checkSimilProfile(Account findAccount, Profile findProfile, List<CheckProfile> allProfile) {

        IdealType findIdealType;
        CheckSimilarity checkSimilarity = new CheckSimilarity();
        try {
            findIdealType = idealTypeRepository.findById(findAccount.getIdealType().getId()).get();
        } catch (NullPointerException e) {
            List<CheckProfile> recommemdProfile = checkSimilarity.recommendProfile(allProfile, findProfile);
            return recommemdProfile;
        }
        List<CheckProfile> recommendProfile = checkSimilarity.recommendProfile2(allProfile, findProfile, findIdealType);

        return recommendProfile;
    }
}
