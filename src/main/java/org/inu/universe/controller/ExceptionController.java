package org.inu.universe.controller;

import lombok.RequiredArgsConstructor;
import org.inu.universe.exception.AccessDeniedException;
import org.inu.universe.exception.AuthenticationException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exception")
@CrossOrigin
public class ExceptionController {

    @GetMapping("/entrypoint")
    public void entrypointException() {
        throw new AuthenticationException("인증 오류 발생");
    }

    @GetMapping("/accessdenied")
    public void accessdeniedException() {
        throw new AccessDeniedException("권한 오류 발생");
    }
}
