package org.inu.universe.service.impl;

import org.inu.universe.domain.Email;
import org.inu.universe.domain.status.EmailStatus;
import org.inu.universe.repository.EmailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.inu.universe.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;
    @Mock
    private EmailRepository emailRepository;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private RedisUtil redisUtil;

    @Test
    @DisplayName("이메일 전송")
    void sendEmail() {

        given(emailRepository.save(any())).willReturn(EMAIL_2);
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) notNull()));

        Email email = emailService.sendEmail(EMAIL_SAVE_REQUEST);

        assertThat(email.getId()).isEqualTo(1L);
        assertThat(email.getAddress()).isEqualTo(EMAIL_SAVE_REQUEST.getAddress());
        assertThat(email.getStatus()).isEqualTo(EmailStatus.NOTAUTH);

        then(emailRepository).should(times(1)).save(any());
    }

    @Test
    @DisplayName("이메일 인증")
    void authEmail() {

        given(emailRepository.findById(any())).willReturn(Optional.of(EMAIL_2));
        when(redisUtil.getData(any())).thenReturn(EMAIL_AUTH_REQUEST.getAuthKey());

        Email email = emailService.authEmail(EMAIL_AUTH_REQUEST);

        assertThat(email.getStatus()).isEqualTo(EmailStatus.AUTH);

        then(emailRepository).should(times(1)).findById(any());
    }
}