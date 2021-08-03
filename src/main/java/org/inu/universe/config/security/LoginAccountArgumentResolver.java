package org.inu.universe.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginAccountArgumentResolver implements HandlerMethodArgumentResolver {

    // 현재 파라미터(Long Type)를 resolver이 지원하는지
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //boolean isUserClass = User.class.equals(parameter.getParameterType());

        boolean isLoginAccountAnnotation = parameter.getParameterAnnotation(LoginAccount.class) != null;
        boolean isLongClass = Long.class.equals(parameter.getParameterType());
        return isLoginAccountAnnotation && isLongClass;
    }

    // 실제로 바인딩 할 객체
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return Long.valueOf(user.getUsername());

//        User user = (User) authentication.getPrincipal();
//        return user;
    }
}
