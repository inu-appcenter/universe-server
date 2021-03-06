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
import org.inu.universe.model.profile.*;
import org.inu.universe.repository.*;
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

//    @PostConstruct
//    void init() {
//
//        List<HashTag> hashTagList = Arrays.asList(HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"), HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"), HashTag.saveHashTag("#?????????"),
//                HashTag.saveHashTag("#?????????"), HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"), HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"), HashTag.saveHashTag("#?????????"),
//                HashTag.saveHashTag("#??????"), HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"),
//                HashTag.saveHashTag("#??????"));
//
//        hashTagRepository.saveAll(hashTagList);
//    }

    /*
    ????????? ??????
     */
    @Override
    @Transactional
    public ProfileResponse saveProfile(Long accountId, ProfileSaveRequest request) {

        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("???????????? ?????? ???????????????."));

        // - ????????? ?????? ??????
        if (profileRepository.findByNickName(request.getNickName()).isPresent()) {
            throw new ProfileException("?????? ???????????? ??????????????????.");
        }

        Profile profile = Profile.saveProfile(request.getNickName(), request.getAge(), request.getGender(), request.getCollege(), request.getMajor());
        Profile savedProfile = profileRepository.save(profile);
        findAccount.setProfile(savedProfile);

        return ProfileResponse.from(savedProfile);
    }

    /*
    ???????????? ??????
    */
    @Override
    public List<HashTagResponse> findHashTag(HashTagSaveRequest request) {

        List<String> hashTagList = request.getHashTagList();
        // - ???????????? ?????? 3???.
        if (hashTagList.size() > 3) {
            throw new HashTagException("??????????????? ?????? 3????????? ???????????????.");
        }

        List<HashTag> findHashTagList = new ArrayList<>();

        for (String name : hashTagList) {
            HashTag hashTag = hashTagRepository.findByName(name)
                    .orElseThrow(() -> new HashTagException("???????????? ?????? ?????????????????????."));
            findHashTagList.add(hashTag);
        }

        return findHashTagList.stream().map(hashTag -> HashTagResponse.from(hashTag)).collect(Collectors.toList());
    }

    /*
    ????????? ?????? ??? ?????? ??????
     */
    @Override
    @Transactional
    public ProfileResponse updateProfile(Long accountId, MultipartFile image, ProfileUpdateRequest request) {

        Account findAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("???????????? ?????? ???????????????."));

        Profile findProfile = profileRepository.findById(findAccount.getProfile().getId())
                .orElseThrow(() -> new ProfileException("???????????? ?????? ??????????????????."));

        // - ????????? ?????? ??????
        if (profileRepository.findByNickName(request.getNickName()).isPresent() && !findProfile.getNickName().equals(request.getNickName())) {
            throw new ProfileException("?????? ???????????? ??????????????????.");
        }

        // - ????????? ?????? ??????
        if (findProfile.getProfileImage() != null) {
            deleteImageS3(findProfile.getProfileImage());
        }
        ProfileImage imageObject = uploadImageS3(image);

        // - ???????????? ??????
        List<String> hashTagList = request.getHashTagList();

        profileTagRepository.deleteByProfile(findProfile);
        profileTagRepository.flush();

        for (String name : hashTagList) {
            HashTag hashTag = hashTagRepository.findByName(name)
                    .orElseThrow(() -> new HashTagException("???????????? ?????? ?????????????????????."));
            ProfileTag profileTag = ProfileTag.saveProfileTag(findProfile, hashTag);
            profileTagRepository.save(profileTag);
        }

        // - ????????? ?????? ??? ?????? ??????
        findProfile.updateProfile(imageObject, request.getNickName(), request.getAge(), request.getGender(), request.getCollege(), request.getMajor(), request.getRegion(), request.getHeight(), request.getBodyType(), request.getMbti(), request.getIntroduction(), request.isProfilePrivate());

        return ProfileResponse.from(findProfile);
    }

    /*
    ????????? ??????
     */
    @Override
    public ProfileResponse findProfile(Long profileId) {

        Profile findProfile = profileQueryRepository.findWithProfileTags(profileId);

        if (findProfile == null) {
            throw new ProfileException("???????????? ?????? ??????????????????.");
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
