package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.service.AccountService;
import org.inu.universe.service.RecommendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AccountService accountService;
    private final RecommendService recommendService;

    // - 계정 삭제
    @DeleteMapping("/admin/account/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccountByAdmin(accountId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    // - 프로필 추천 실행
    @PostMapping("/admin/recommend")
    public ResponseEntity recommendProfile() {
        recommendService.recommendProfile();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
