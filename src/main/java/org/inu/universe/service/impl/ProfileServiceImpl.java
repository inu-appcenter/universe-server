package org.inu.universe.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inu.universe.domain.*;
import org.inu.universe.exception.AccountException;
import org.inu.universe.exception.HashTagException;
import org.inu.universe.exception.ProfileException;
import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.model.hashtag.HashTagSaveRequest;
import org.inu.universe.model.profile.ProfileResponse;
import org.inu.universe.model.profile.ProfileSaveRequest;
import org.inu.universe.model.profile.ProfileUpdateRequest;
import org.inu.universe.repository.AccountRepository;
import org.inu.universe.repository.HashTagRepository;
import org.inu.universe.repository.ProfileRepository;
import org.inu.universe.repository.ProfileTagRepository;
import org.inu.universe.repository.query.ProfileQueryRepository;
import org.inu.universe.service.ProfileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;
    private final HashTagRepository hashTagRepository;
    private final ProfileTagRepository profileTagRepository;
    private final ProfileQueryRepository profileQueryRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.thumbnailBucket}")
    private String thumbnailBucket;

    @PostConstruct
    void init() {

        List<HashTag> hashTagList = Arrays.asList(HashTag.saveHashTag("#만화"),
                HashTag.saveHashTag("#취준"), HashTag.saveHashTag("#운동"),
                HashTag.saveHashTag("#공부"), HashTag.saveHashTag("#유튜브"),
                HashTag.saveHashTag("#민초단"), HashTag.saveHashTag("#패션"),
                HashTag.saveHashTag("#요리"), HashTag.saveHashTag("#독서"),
                HashTag.saveHashTag("#연애"), HashTag.saveHashTag("#강아지"),
                HashTag.saveHashTag("#맛집"), HashTag.saveHashTag("#음악"),
                HashTag.saveHashTag("#등산"),
                HashTag.saveHashTag("#수영"));

        hashTagRepository.saveAll(hashTagList);
    }

    /*
    프로필 설정
     */
    @Override
    @Transactional
    public ProfileResponse saveProfile(Long accountId, ProfileSaveRequest request) {

        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        // - 닉네임 중복 확인
        if (profileRepository.findByNickName(request.getNickName()).isPresent()) {
            throw new ProfileException("이미 존재하는 닉네임입니다.");
        }

        Profile profile = Profile.saveProfile(request.getNickName(), request.getAge(), request.getGender(),request.getCollege(), request.getMajor());
        Profile savedProfile = profileRepository.save(profile);
        findAccount.setProfile(savedProfile);

        return ProfileResponse.from(savedProfile);
    }

    /*
    해시태그 선택
    */
    @Override
    public List<HashTagResponse> findHashTag(HashTagSaveRequest request) {

        List<String> hashTagList = request.getHashTagList();
        // - 해시태그 최대 3개.
        if (hashTagList.size() > 3) {
            throw new HashTagException("해시태그는 최대 3개까지 가능합니다.");
        }

        List<HashTag> findHashTagList = new ArrayList<>();

        for (String name : hashTagList) {
            HashTag hashTag = hashTagRepository.findByName(name)
                    .orElseThrow(() -> new HashTagException("존재하지 않는 해시태그입니다."));
            findHashTagList.add(hashTag);
        }

        return findHashTagList.stream().map(hashTag -> HashTagResponse.from(hashTag)).collect(Collectors.toList());
    }

    /*
    프로필 수정 및 설정 추가
     */
    @Override
    @Transactional
    public ProfileResponse updateProfile(Long profileId, MultipartFile image, ProfileUpdateRequest request) {

        Profile findProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileException("존재하지 않는 게시글입니다."));

        // - 닉네임 중복 확인
        if (profileRepository.findByNickName(request.getNickName()).isPresent() && !findProfile.getNickName().equals(request.getNickName())) {
            throw new ProfileException("이미 존재하는 닉네임입니다.");
        }

        // - 프로필 사진 등록
        if (findProfile.getProfileImage() != null) {
            deleteImageS3(findProfile.getProfileImage());
        }
        ProfileImage imageObject = uploadImageS3(image);

        // - 해시태그 설정
        List<String> hashTagList = request.getHashTagList();

        profileTagRepository.deleteByProfile(findProfile);
        profileTagRepository.flush();

        for (String name : hashTagList) {
            HashTag hashTag = hashTagRepository.findByName(name)
                    .orElseThrow(() -> new HashTagException("존재하지 않는 해시태그입니다."));
            ProfileTag profileTag = ProfileTag.saveProfileTag(findProfile, hashTag);
            profileTagRepository.save(profileTag);
        }

        // - 프로필 수정 및 설정 추가
        findProfile.updateProfile(imageObject, request.getNickName(), request.getAge(), request.getGender(), request.getCollege(), request.getMajor(), request.isMilitaryStatus(),
                request.isGraduationStatus(), request.getRegion(), request.getHeight(), request.getBodyType(), request.getMbti(), request.getIntroduction(), request.isProfilePrivate());

        return ProfileResponse.from(findProfile);
    }

    /*
    프로필 조회
     */
    @Override
    public ProfileResponse findProfile(Long profileId) {

        Profile findProfile = profileQueryRepository.findWithProfileTags(profileId);

        if (findProfile == null) {
            throw new ProfileException("존재하지 않는 프로필입니다.");
        }

        if (findProfile.isProfilePrivate()) {
            Profile privateProfile = Profile.setPrivate(findProfile.getId(), findProfile.getNickName(), findProfile.getAge(), findProfile.getGender(), findProfile.getCollege(), findProfile.getMajor());
            return ProfileResponse.from(privateProfile);
        }

        return ProfileResponse.from(findProfile);
    }

    private ProfileImage uploadImageS3(MultipartFile image) {

        ProfileImage imageObject = null;

        if (!image.isEmpty()) {
            if (!image.getContentType().startsWith("image")) {
                throw new IllegalStateException();
            }
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(image.getSize());
            objectMetadata.setContentType(image.getContentType());
            String imageStoreName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            try {
                amazonS3Client.putObject(new PutObjectRequest(bucket, imageStoreName, image.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
                String profileImageUrl = amazonS3Client.getUrl(bucket, imageStoreName).toString();
                String thumbnailImageUrl = amazonS3Client.getUrl(thumbnailBucket, imageStoreName).toString();

                imageObject = ProfileImage.saveImage(imageStoreName, profileImageUrl, thumbnailImageUrl);


            } catch (IOException exception) {
                log.error(exception.getMessage());
            }
        }
        return imageObject;
    }

    private void deleteImageS3(ProfileImage image) {
        amazonS3Client.deleteObject(bucket, image.getImageStoreName());
        amazonS3Client.deleteObject(thumbnailBucket, image.getImageStoreName());
    }
}
