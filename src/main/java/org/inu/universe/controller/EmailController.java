package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.email.EmailRequest;
import org.inu.universe.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class EmailController{

    private final EmailService emailService;

    /*
    이메일 전송
    */
    @PostMapping("/email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid EmailRequest request) {
        emailService.sendEmail(request);
        return ResponseEntity.ok().build();
    }

    /*
    이메일 인증
     */
    @PostMapping("/email/auth")
    public ResponseEntity authEmail(@RequestBody @Valid EmailSaveRequest request) {
        emailService.authEmail(request);
        return ResponseEntity.ok().build();
    }
}
