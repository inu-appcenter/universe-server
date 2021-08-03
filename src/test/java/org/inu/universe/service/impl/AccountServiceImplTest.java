package org.inu.universe.service.impl;

import org.inu.universe.config.security.JwtTokenProvider;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.token.TokenDto;
import org.inu.universe.repository.AccountRepository;
import org.inu.universe.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.inu.universe.TestFixture.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("계정 생성(회원 가입)")
    public void saveAccount() {

        given(emailRepository.findByAddress(any())).willReturn(Optional.of(EMAIL));
        given(accountRepository.findByEmail(any())).willReturn(Optional.empty());
        given(accountRepository.save(any())).willReturn(ACCOUNT);

        AccountResponse savedAccount = accountService.saveAccount(ACCOUNT_SAVE_REQUEST);

        assertThat(savedAccount.getId()).isEqualTo(ACCOUNT.getId());
        assertThat(savedAccount.getEmail()).isEqualTo(EMAIL);
        assertThat(savedAccount.getPassword()).isEqualTo(ACCOUNT_SAVE_REQUEST.getPassword());
        assertThat(savedAccount.getRefreshToken()).isNull();
        assertThat(savedAccount.getStatus()).isEqualTo(ACCOUNT.getStatus());
        assertThat(savedAccount.getRole()).isEqualTo(ACCOUNT.getRole());

        then(emailRepository).should(times(1)).findByAddress(any());
        then(accountRepository).should(times(1)).findByEmail(any());
        then(accountRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("계정 로그인")
    public void loginAccount() {

        given(emailRepository.findByAddress(any())).willReturn(Optional.of(EMAIL));
        given(accountRepository.findByEmail(any())).willReturn(Optional.of(ACCOUNT));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(any())).thenReturn(TOKEN_DTO.getAccessToken());
        when(jwtTokenProvider.createRefreshToken(any())).thenReturn(TOKEN_DTO.getRefreshToken());

        TokenDto tokenDto = accountService.loginAccount(ACCOUNT_LOGIN_REQUEST);

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
        assertThat(ACCOUNT.getRefreshToken()).isEqualTo(tokenDto.getRefreshToken());

        then(emailRepository).should(times(1)).findByAddress(any());
        then(accountRepository).should(times(1)).findByEmail(any());
    }

    @Test
    @DisplayName("AccessToken 재발급")
    public void reissue() {

        given(accountRepository.findById(any())).willReturn(Optional.of(ACCOUNT_2));
        when(jwtTokenProvider.validateToken(any())).thenReturn(true);
        when(jwtTokenProvider.createAccessToken(any())).thenReturn("1234");

        String reissue = accountService.reissue(ACCOUNT_2.getId(), ACCOUNT_2.getRefreshToken());

        assertThat(reissue).isEqualTo("1234");

        then(accountRepository).should(times(1)).findById(any());
    }
}