package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.config.security.SecurityConfig;
import org.inu.universe.model.profile.ProfileDto;
import org.inu.universe.service.impl.RecommendServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.inu.universe.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class RecommendControllerTest {
    @MockBean
    private RecommendServiceImpl recommendService;
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
    @DisplayName("원하는 상대 추천 프로필 조회")
    public void findRecommendProfile() throws Exception {
        List<ProfileDto> profileDtos = asList(PROFILE_DTO, PROFILE_DTO_2, PROFILE_DTO_3, PROFILE_DTO_4, PROFILE_DTO_5);
        String response = objectMapper.writeValueAsString(profileDtos);

        given(recommendService.findRecommendProfile(any())).willReturn(profileDtos);

        mockMvc.perform(get("/recommend")
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("recommend/findRecommendList",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("프로필 ID"),
                                fieldWithPath("[].profileImage").type(JsonFieldType.OBJECT).description("프로필 사진").optional(),
                                fieldWithPath("[].profileImage.imageStoreName").type(JsonFieldType.STRING).description("이미지 저장된 이름"),
                                fieldWithPath("[].profileImage.profileImageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("[].profileImage.thumbnailImageUrl").type(JsonFieldType.STRING).description("썸네일 이미지 URL"),
                                fieldWithPath("[].nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[].age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("[].gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("[].major").type(JsonFieldType.STRING).description("학과")
                        )));

        then(recommendService).should(times(1)).findRecommendProfile(any());
    }

    @Test
    @DisplayName("원하는 상대 [관심없음]")
    public void dislikeProfile() throws Exception {
        willDoNothing().given(recommendService).dislikeProfile(any(), any());

        mockMvc.perform(delete("/recommend/{profileId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken"))
                .andExpect(status().isOk())
                .andDo(document("recommend/dislike",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("profileId").description("프로필 Id")
                        )));

        then(recommendService).should(times(1)).dislikeProfile(any(), any());
    }
}