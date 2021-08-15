package org.inu.universe.service.impl;

import lombok.RequiredArgsConstructor;
import org.inu.universe.config.security.JwtTokenProvider;
import org.inu.universe.domain.Email;
import org.inu.universe.domain.Account;
import org.inu.universe.exception.EmailException;
import org.inu.universe.exception.AccountException;
import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.token.TokenDto;
import org.inu.universe.repository.EmailRepository;
import org.inu.universe.repository.AccountRepository;
import org.inu.universe.service.AccountService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)     // transection 커밋시 자동으로 flush(변경 내용 DB에 반영)가 호출되지 않음
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EmailRepository emailRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /*
    Admin 계정 생성
     */


    /*
    회원 가입
     */
    @Override
    @Transactional
    public AccountResponse saveAccount(AccountSaveRequest request) {

        // - 비밀번호와 재확인 비밀번호 일치 확인
        reconfirmPassword(request.getPassword(), request.getPassword2());

        // - 인증된 이메일인지 확인
        Email email = emailRepository.findByAddress(request.getAddress())
                .orElseThrow(() -> new EmailException("이메일 인증을 완료해주세요."));

        // - 이미 가입된 계정인지 확인
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new AccountException("이미 존재하는 계정입니다.");
        }

        // - 회원 저장
        Account account = Account.saveAccount(email, passwordEncoder.encode(request.getPassword()));
        return AccountResponse.from(accountRepository.save(account));
    }

    /*
    회원 로그인
     */
    @Override
    @Transactional
    public TokenDto loginAccount(AccountLoginRequest request) {

        // - 이메일, 계정이 존재하는지 확인
        Email email = emailRepository.findByAddress(request.getAddress())
                .orElseThrow(() -> new EmailException("이메일 인증을 완료해주세요."));

        Account findAccount = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        // - 비밀번호 일치 확인
        if (!passwordEncoder.matches(request.getPassword(), findAccount.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        // - access & refresh Token 발급
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(findAccount.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken(String.valueOf(findAccount.getId()));
        findAccount.setRefreshToken(refreshToken);

        TokenDto tokenDto = TokenDto.from(accessToken, refreshToken);

        return tokenDto;
    }

    /*
    AccessToken 재발급
     */
    @Override
    @Transactional
    public String reissue(Long accountId, String refreshToken) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountException("존재하지 않는 계정입니다."));

        // - DB의 refreshToken과 일치하는지 확인
        if (!account.getRefreshToken().equals(refreshToken)) {
            throw new AccountException("계정의 RefreshToken과 일치하지 않습니다.");
        }

        // - 해당 refreshToken이 유효한지 확인
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AccountException("RefreshToken이 유효하지 않습니다.");
        }

        // - AccessToken 재발급
        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(accountId));

//        TokenDto tokenDto = TokenDto.from(accessToken, refreshToken);

        return accessToken;
    }

    // 비밀번호 확인
    private void reconfirmPassword(String password, String password2) {

        if (!password.equals(password2)) {
            throw new AccountException("비밀번호가 서로 일치하지 않습니다.");
        }
    }
}
