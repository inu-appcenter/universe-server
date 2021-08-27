package org.inu.universe.service.impl;

import org.inu.universe.domain.IdealType;
import org.inu.universe.domain.Profile;
import org.inu.universe.model.profile.CheckProfile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CheckSimilarity {

    // - IdealType 설정 X인 경우
    public List<CheckProfile> recommendProfile(List<CheckProfile> allProfile, Profile findProfile) {

        List<String> profileTagList = findProfile.getProfileTagList().stream().map(profileTag -> profileTag.getHashTag().getName()).collect(Collectors.toList());
        List<CheckProfile> recommendList = new ArrayList<>();

        Collections.shuffle(allProfile);

        for (CheckProfile profile : allProfile) {

            if (profile.getId().equals(findProfile.getId())) {
                continue;
            }
            double result = 0;

            double TagScore = checkTagScore(profileTagList, profile.getHashTagList());

            result += TagScore;
            profile.setSimilarity(result);
            recommendList.add(profile);
        }

        recommendList.sort(new Comparator<CheckProfile>() {
            @Override
            public int compare(CheckProfile o1, CheckProfile o2) {
                double x = o1.getSimilarity();
                double y = o2.getSimilarity();
                if (x > y) return -1;
                if (x < y) return 1;
                else return 0;
            }
        });

        if (recommendList.size() <= 5) {
            return recommendList;
        }

        return recommendList.subList(0, 5);
    }

    // - IdealType 설정 O인 경우
    public List<CheckProfile> recommendProfile2(List<CheckProfile> allProfile, Profile findProfile, IdealType findIdealType) {

        List<String> profileTagList = findProfile.getProfileTagList().stream().map(profileTag -> profileTag.getHashTag().getName()).collect(Collectors.toList());
        List<CheckProfile> recommendList = new ArrayList<>();

        Collections.shuffle(allProfile);
        for (CheckProfile profile : allProfile) {

            if (profile.getId().equals(findProfile.getId())) {
                continue;
            }
            double result = 0;

            double TagScore = checkTagScore(profileTagList, profile.getHashTagList());
            double IdealScore = checkIdealTypeScore(findIdealType, profile);

            result += TagScore + IdealScore;
            profile.setSimilarity(result);
            recommendList.add(profile);
        }

        recommendList.sort(new Comparator<CheckProfile>() {
            @Override
            public int compare(CheckProfile o1, CheckProfile o2) {
                double x = o1.getSimilarity();
                double y = o2.getSimilarity();
                if (x > y) return -1;
                if (x < y) return 1;
                else return 0;
            }
        });

        if (recommendList.size() <= 5) {
            return recommendList;
        }

        return recommendList.subList(0, 5);
    }

    // - 원하는 상대 설정된 조건과 비교
    private double checkIdealTypeScore(IdealType findIdealType, CheckProfile profile) {
        int sum = 0;

        if (!findIdealType.getGender().equals("무관")) {
            if (findIdealType.getGender().equals(profile.getGender())) { sum += 1; } }

        if (profile.getRegion() != null) {
            if (findIdealType.getRegion().equals(profile.getRegion())) { sum += 1; } }

        if (profile.getAge() >= findIdealType.getAge1() && profile.getAge() <= findIdealType.getAge2()) { sum += 1; }

        return sum;
    }
    // - 해시태그 비교
    private double checkTagScore(List<String> profileTagList, List<String> hashTagList) {

        int sum = 0;

        if (profileTagList.size() == 0 || hashTagList.size() == 0) { return 0; }

        for (String name : hashTagList) {
            if (profileTagList.contains(name)) { sum += 1; }
        }

        return sum;
    }
}
