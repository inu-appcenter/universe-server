package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.JwtTokenProvider;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.service.impl.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.inu.universe.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class EmailControllerTest {

    @MockBean
    private EmailServiceImpl emailService;
    @MockBean
    private LoginAccountArgumentResolver loginAccountArgumentResolver;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

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
    @DisplayName("이메일 전송")
    void sendEmail() throws Exception {

        String body = objectMapper.writeValueAsString(EMAIL_REQUEST);


        mockMvc.perform(post("/email")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(document("email/send",
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("학교 이메일 주소")
                        )));

        then(emailService).should(times(1)).sendEmail(any());
    }

    @Test
    @DisplayName("이메일 인증")
    void authEmail() throws Exception {

        String body = objectMapper.writeValueAsString(EMAIL_SAVE_REQUEST);

        given(emailService.authEmail(any())).willReturn(EMAIL);

        mockMvc.perform(post("/email/auth")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(document("email/auth",
                        requestFields(
                                fieldWithPath("address").type(JsonFieldType.STRING).description("학교 이메일 주소"),
                                fieldWithPath("authKey").type(JsonFieldType.STRING).description("인증 번호")
                        )));
        then(emailService).should(times(1)).authEmail(any());
    }
}