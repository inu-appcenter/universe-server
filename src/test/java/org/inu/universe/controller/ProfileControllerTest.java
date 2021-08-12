package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.config.security.SecurityConfig;
import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.service.impl.ProfileServiceImpl;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.Arrays.*;
import static org.inu.universe.TestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProfileController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class ProfileControllerTest {

    @MockBean
    private ProfileServiceImpl profileService;
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
    @DisplayName("프로필 설정")
    void saveProfile() throws Exception {

        String body = objectMapper.writeValueAsString(PROFILE_SAVE_REQUEST);

        String response = objectMapper.writeValueAsString(PROFILE_RESPONSE);

        given(profileService.saveProfile(any(), any())).willReturn(PROFILE_RESPONSE);

        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(post("/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("profile/save",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임 (필수, 중복X)"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이 (필수)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (필수)"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("학과 (필수)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로필 ID"),
                                fieldWithPath("profileImage").type(JsonFieldType.OBJECT).description("프로필 사진 (null)").optional(),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("학과"),
                                fieldWithPath("militaryStatus").type(JsonFieldType.BOOLEAN).description("병역필 (false)"),
                                fieldWithPath("graduationStatus").type(JsonFieldType.BOOLEAN).description("졸업유무 (false)"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역 (null)").optional(),
                                fieldWithPath("height").type(JsonFieldType.STRING).description("키 (null)").optional(),
                                fieldWithPath("bodyType").type(JsonFieldType.STRING).description("체형 (null)").optional(),
                                fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI (null)").optional(),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개 (null)").optional(),
                                fieldWithPath("hashTagList").type(JsonFieldType.ARRAY).description("해시태그 (empty)").optional()
                        )));

        then(profileService).should(times(1)).saveProfile(any(), any());
    }

    @Test
    @DisplayName("해시태그 선택")
    public void findHashTag() throws Exception {

        String body = objectMapper.writeValueAsString(HASH_TAG_SAVE_REQUEST);

        List<HashTagResponse> hashTagResponseList = asList(HASH_TAG_RESPONSE_1, HASH_TAG_RESPONSE_2, HASH_TAG_RESPONSE_3);

        String response = objectMapper.writeValueAsString(hashTagResponseList);

        given(profileService.findHashTag(any(), any())).willReturn(hashTagResponseList);

        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(get("/profile/hashTags")
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("profile/hashTag",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("hashTagList").type(JsonFieldType.ARRAY).description("해시태그 내용")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("해시태그 Id"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("해시태그 내용")
                        )));

        then(profileService).should(times(1)).findHashTag(any(), any());
    }

    @Test
    @DisplayName("프로필 수정")
    public void updateProfile() throws Exception {

        MockMultipartFile image = new MockMultipartFile("image", "image.png",
                MediaType.IMAGE_PNG_VALUE, "<<image data>>".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile request = new MockMultipartFile("request", "request",
                MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(PROFILE_UPDATE_REQUEST).getBytes(StandardCharsets.UTF_8));

        MockMultipartHttpServletRequestBuilder builder = fileUpload("/profile/{profileId}", 1L);
        builder.with(request1 -> { request1.setMethod(String.valueOf(HttpMethod.PATCH)); return request1; });

        String response = objectMapper.writeValueAsString(PROFILE_RESPONSE_2);

        given(profileService.updateProfile(any(), any(), any(), any())).willReturn(PROFILE_RESPONSE_2);

        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(builder.file(image).file(request)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("profile/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("profileId").description("프로필 Id")
                        ),
                        requestParts(
                                partWithName("image").description("이미지 파일").optional(),
                                partWithName("request").description("프로필 수정 요청")
                        ),
                        requestPartFields("request",
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네일 (필수)"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이 (필수)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별 (필수)"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("학과 (필수)"),
                                fieldWithPath("militaryStatus").type(JsonFieldType.BOOLEAN).description("병역필"),
                                fieldWithPath("graduationStatus").type(JsonFieldType.BOOLEAN).description("졸업유무"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역").optional(),
                                fieldWithPath("height").type(JsonFieldType.STRING).description("키").optional(),
                                fieldWithPath("bodyType").type(JsonFieldType.STRING).description("체형").optional(),
                                fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI").optional(),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개").optional(),
                                fieldWithPath("hashTagList").type(JsonFieldType.ARRAY).description("해시태그").optional()
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로필 ID"),
                                fieldWithPath("profileImage").type(JsonFieldType.OBJECT).description("프로필 사진").optional(),
                                fieldWithPath("profileImage.imageStoreName").type(JsonFieldType.STRING).description("이미지 저장된 이름"),
                                fieldWithPath("profileImage.profileImageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("profileImage.thumbnailImageUrl").type(JsonFieldType.STRING).description("썸네일 이미지 URL"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("학과"),
                                fieldWithPath("militaryStatus").type(JsonFieldType.BOOLEAN).description("병역필"),
                                fieldWithPath("graduationStatus").type(JsonFieldType.BOOLEAN).description("졸업유무"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역").optional(),
                                fieldWithPath("height").type(JsonFieldType.STRING).description("키").optional(),
                                fieldWithPath("bodyType").type(JsonFieldType.STRING).description("체형").optional(),
                                fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI").optional(),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개").optional(),
                                fieldWithPath("hashTagList").type(JsonFieldType.ARRAY).description("해시태그").optional()
                        )));

        then(profileService).should(times(1)).updateProfile(any(), any(), any(), any());
    }

    @Test
    @DisplayName("프로필 조회")
    public void findProfile() throws Exception{

        String response = objectMapper.writeValueAsString(PROFILE_RESPONSE_2);

        given(profileService.findProfile(any(), any())).willReturn(PROFILE_RESPONSE_2);

        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(get("/profile/{profileId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken"))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("profile/findProfile",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("profileId").description("프로필 Id")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("프로필 ID"),
                                fieldWithPath("profileImage").type(JsonFieldType.OBJECT).description("프로필 사진").optional(),
                                fieldWithPath("profileImage.imageStoreName").type(JsonFieldType.STRING).description("이미지 저장된 이름"),
                                fieldWithPath("profileImage.profileImageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                                fieldWithPath("profileImage.thumbnailImageUrl").type(JsonFieldType.STRING).description("썸네일 이미지 URL"),
                                fieldWithPath("nickName").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("major").type(JsonFieldType.STRING).description("학과"),
                                fieldWithPath("militaryStatus").type(JsonFieldType.BOOLEAN).description("병역필"),
                                fieldWithPath("graduationStatus").type(JsonFieldType.BOOLEAN).description("졸업유무"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역").optional(),
                                fieldWithPath("height").type(JsonFieldType.STRING).description("키").optional(),
                                fieldWithPath("bodyType").type(JsonFieldType.STRING).description("체형").optional(),
                                fieldWithPath("mbti").type(JsonFieldType.STRING).description("MBTI").optional(),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개").optional(),
                                fieldWithPath("hashTagList").type(JsonFieldType.ARRAY).description("해시태그").optional()
                        )));


        then(profileService).should(times(1)).findProfile(any(), any());
    }
}