package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.LoginAccount;
import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.model.hashtag.HashTagSaveRequest;
import org.inu.universe.model.profile.ProfileResponse;
import org.inu.universe.model.profile.ProfileSaveRequest;
import org.inu.universe.model.profile.ProfileUpdateRequest;
import org.inu.universe.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /*
    프로필 설정 ( 닉네임, 나이, 성별, 학과 )
     */
    @PostMapping("/profile")
    public ResponseEntity<ProfileResponse> saveProfile(@LoginAccount Long accountId, @RequestBody @Valid ProfileSaveRequest request) {
        return ResponseEntity.ok(profileService.saveProfile(accountId, request));
    }

    /*
    해시태그 선택
     */
    @GetMapping("/profile/hashTags")
    public ResponseEntity<List<HashTagResponse>> findHashTag(@RequestBody @Valid HashTagSaveRequest request) {
        return ResponseEntity.ok(profileService.findHashTag(request));
    }

    /*
    프로필 수정 ( 닉네임, 나이, 성별, 학과 ) 및 설정 추가 ( 프로필 사진, 병역필, 졸업유무, 지역, 키, 체형, mbti, 소개, 해시태그 )
     */
    @PatchMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@LoginAccount Long accountId, @RequestPart(required = false) MultipartFile image, @RequestPart @Valid ProfileUpdateRequest request) {
        return ResponseEntity.ok(profileService.updateProfile(accountId, image, request));
    }

    /*
    프로필 조회
     */
    @GetMapping("/profile/{profileId}")
    public ResponseEntity<ProfileResponse> findProfile(@PathVariable Long profileId) {
        return ResponseEntity.ok(profileService.findProfile(profileId));
    }
}
