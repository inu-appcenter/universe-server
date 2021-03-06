package org.inu.universe.service.impl;

import lombok.RequiredArgsConstructor;
import org.inu.universe.domain.Email;
import org.inu.universe.exception.EmailException;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.email.EmailRequest;
import org.inu.universe.repository.EmailRepository;
import org.inu.universe.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    /*
    이메일 전송
    */
    @Override
    @Transactional
    public void sendEmail(EmailRequest request) {

        // - 이메일이 인천대학교 이메일인지 확인
        if (!request.getAddress().endsWith("inu.ac.kr")) {
            throw new EmailException("학교 이메일 주소를 적어주세요.");
        }

        // 임의의 authKey 생성
        Random random = new Random();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);      // 범위 : 111111 ~ 999999

        // 이메일 전송
        sendAuthEmail(request.getAddress(), authKey);
    }

    /*
    이메일 인증
    */
    @Override
    @Transactional
    public Email authEmail(EmailSaveRequest request) {
        String authKey = request.getAuthKey();
        String saveEmailId = redisUtil.getData(authKey);
        if (saveEmailId == null) {
            throw new EmailException("인증 번호가 일치하지 않습니다.");
        }

        // - 이미 인증된 이메일인 경우
        if (emailRepository.findByAddress(request.getAddress()).isPresent()) {
            return emailRepository.findByAddress(request.getAddress()).get();
        }

        // - 이메일 저장
        Email email = Email.saveEmail(request.getAddress());

        Email savedEmail = emailRepository.save(email);

        redisUtil.deleteData(authKey);

        return savedEmail;
    }

    // 이메일 전송
    private void sendAuthEmail(String address, String authKey) {

        String subject = "[유니버스] 회원 가입 이메일 인증번호입니다.";
        String text = "회원 가입을 위한 인증번호는 " + authKey + "입니다. <br/>" + "해당 인증번호를 인증번호 입력란에 기입해주세요.<br/>" + "유니버스에 가입해주셔서 감사합니다.";

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(address);
            helper.setSubject(subject);
            helper.setText(text, true);  //포함된 텍스트가 HTML이라는 의미로 true.
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 유효 시간(5분)동안 {email, authKey} 저장
        redisUtil.setDataExpire(authKey, address, 60 * 5L);
    }
}
