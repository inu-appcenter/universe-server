package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.LoginAccount;
import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.token.TokenDto;
import org.inu.universe.service.AccountService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor          // 초기화 되지않은 final 필드나, @NonNull이 붙은 필드에 대해 생성자를 생성
public class AccountController {

    private final AccountService accountService;

    /*
    회원 가입
     */
    @PostMapping("/account")
    public ResponseEntity saveAccount(@RequestBody @Valid AccountSaveRequest request) {
        accountService.saveAccount(request);
        return ResponseEntity.ok().build();
    }

    /*
    회원 로그인 ( 토큰 발행 )
     */
    @PostMapping("/account/login")
    public ResponseEntity loginAccount(@RequestBody @Valid AccountLoginRequest request) {

        TokenDto tokenDto = accountService.loginAccount(request);

        return ResponseEntity.ok()
                .header("accessToken", tokenDto.getAccessToken())
                .header("refreshToken", tokenDto.getRefreshToken())
                .build();

//        return ResponseEntity.ok(tokenDto); [body]
    }

    /*
    AccessToken 만료 시,
    RefreshToken을 통해 AccessToken 재발급  // RefreshToken 만료 시, /account/login
     */
    @PostMapping("/account/reissue")
    public ResponseEntity reissue(@RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken) {

        String reissueToken = accountService.reissue(refreshToken.substring(7));

        return ResponseEntity.ok()
                .header("accessToken", reissueToken)
                .build();
    }

    /*
    회원 프로필 Id, 원하는 상대 Id 조회
     */
    @GetMapping("/account")
    public ResponseEntity<AccountResponse> findId(@LoginAccount Long accountId) {
        return ResponseEntity.ok(accountService.findId(accountId));
    }
}
