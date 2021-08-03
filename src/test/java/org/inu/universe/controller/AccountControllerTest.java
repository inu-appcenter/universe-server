package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.config.security.SecurityConfig;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;


import static org.inu.universe.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTest {

    @MockBean
    private AccountServiceImpl accountService;
    @MockBean
    private LoginAccountArgumentResolver loginAccountArgumentResolver;
    @MockBean
    private SecurityConfig securityConfig;
    @MockBean
    private WebSecurityConfiguration webSecurityConfiguration;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup(WebApplicationContext wac, RestDocumentationContextProvider contextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .alwaysDo(print())
                .apply(documentationConfiguration(contextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("회원 가입")
    void saveAccount() throws Exception {

        String body = objectMapper.writeValueAsString(ACCOUNT_SAVE_REQUEST);

        given(accountService.saveAccount(any())).willReturn(AccountResponse.from(ACCOUNT));

        mockMvc.perform(post("/account")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("account/save",
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("계정 이메일(필수)"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("계정 비밀번호(필수)"),
                                fieldWithPath("password2").type(JsonFieldType.STRING).description("계정 비밀번호 재확인(필수)")
                        )));
        then(accountService).should(times(1)).saveAccount(any());
    }

    @Test
    @DisplayName("회원 로그인")
    void loginAccount() throws Exception {

        String body = objectMapper.writeValueAsString(ACCOUNT_LOGIN_REQUEST);

        given(accountService.loginAccount(any())).willReturn(TOKEN_DTO);

        mockMvc.perform(post("/account/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(document("account/login",
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("계정 이메일(필수)"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("계정 비밀번호(필수)")
                        ),
                        responseHeaders(
                                headerWithName("accessToken").description("AccessToken"),
                                headerWithName("refreshToken").description("RefreshToken")
                        )));

        then(accountService).should(times(1)).loginAccount(any());
    }

    @Test
    @DisplayName("RefreshToken 재발급")
    void reissue() throws Exception {

        given(accountService.reissue(any(), any())).willReturn(REISSUE_ACCESS_TOKEN);
        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(post("/account/reissue")
                .header(HttpHeaders.AUTHORIZATION, TOKEN_DTO.getRefreshToken()))
                .andExpect(status().isOk())
                .andDo(document("account/reissue",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("RefreshToken(필수)")
                        ),
                        responseHeaders(
                                headerWithName("accessToken").description("새로 발급된 AccessToken")
                        )));

        then(accountService).should(times(1)).reissue(any(), any());
    }
}