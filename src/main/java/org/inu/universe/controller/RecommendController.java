package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.LoginAccount;
import org.inu.universe.model.profile.ProfileDto;
import org.inu.universe.service.RecommendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    /*
    원하는 상대 추천 프로필 조회
     */
    @GetMapping("/recommend")
    public ResponseEntity<List<ProfileDto>> findRecommendProfile(@LoginAccount Long accountId) {
        return ResponseEntity.ok(recommendService.findRecommendProfile(accountId));
    }

    /*
    원하는 상대 [관심없음]
     */
    @DeleteMapping("/recommend/{profileId}")
    public ResponseEntity dislikeProfile(@LoginAccount Long accountId, @PathVariable Long profileId) {
        recommendService.dislikeProfile(accountId, profileId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
