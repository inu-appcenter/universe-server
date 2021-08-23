package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.config.security.SecurityConfig;
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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

        willDoNothing().given(accountService).saveAccount(any());

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

        given(accountService.reissue(any())).willReturn(REISSUE_ACCESS_TOKEN);

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

        then(accountService).should(times(1)).reissue(any());
    }

    @Test
    @DisplayName("회원 ID, 프로필 ID, 원하는 상대 ID 조회")
    public void findId() throws Exception {

        String response = objectMapper.writeValueAsString(ACCOUNT_RESPONSE);

        given(accountService.findId(any())).willReturn(ACCOUNT_RESPONSE);

        mockMvc.perform(get("/account")
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("account/findId",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("accountId").description("계정 Id"),
                                fieldWithPath("profileId").description("프로필 Id"),
                                fieldWithPath("idealTypeId").description("원하는 상대 Id")
                        )));

        then(accountService).should(times(1)).findId(any());
    }
}