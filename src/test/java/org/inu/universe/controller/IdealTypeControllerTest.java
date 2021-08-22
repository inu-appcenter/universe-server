package org.inu.universe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.inu.universe.config.security.LoginAccountArgumentResolver;
import org.inu.universe.config.security.SecurityConfig;
import org.inu.universe.repository.ProfileRepository;
import org.inu.universe.service.impl.IdealTypeServiceImpl;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdealTypeController.class)
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class IdealTypeControllerTest {

    @MockBean
    private IdealTypeServiceImpl idealTypeService;
    @MockBean
    private LoginAccountArgumentResolver loginAccountArgumentResolver;
    @MockBean
    private SecurityConfig securityConfig;
    @MockBean
    private WebSecurityConfiguration webSecurityConfiguration;
    @MockBean
    private ProfileRepository profileRepository;

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
    @DisplayName("원하는 상대 설정")
    public void saveIdealType() throws Exception {

        String body = objectMapper.writeValueAsString(IDEAL_TYPE_REQUEST);
        String response = objectMapper.writeValueAsString(IDEAL_TYPE_RESPONSE);

        given(idealTypeService.saveIdealType(any(), any())).willReturn(IDEAL_TYPE_RESPONSE);

        when(loginAccountArgumentResolver.resolveArgument(
                (MethodParameter) notNull()
                , (ModelAndViewContainer) notNull()
                , (NativeWebRequest) notNull()
                , (WebDataBinderFactory) notNull()
        )).thenReturn(1L);

        mockMvc.perform(post("/idealType")
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("idealType/save",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역(필수)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별(필수) [여성/남성/무관]"),
                                fieldWithPath("age1").type(JsonFieldType.NUMBER).description("나이(필수)"),
                                fieldWithPath("age2").type(JsonFieldType.NUMBER).description("나이(필수)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("원하는 상대 Id"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("age1").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("age2").type(JsonFieldType.NUMBER).description("나이")
                        )));

        then(idealTypeService).should(times(1)).saveIdealType(any(), any());
    }

    @Test
    @DisplayName("원하는 상대 수정")
    public void updateIdealType() throws Exception {

        String body = objectMapper.writeValueAsString(IDEAL_TYPE_REQUEST_2);
        String response = objectMapper.writeValueAsString(IDEAL_TYPE_RESPONSE_2);

        given(idealTypeService.updateIdealType(any(), any())).willReturn(IDEAL_TYPE_RESPONSE_2);

        mockMvc.perform(patch("/idealType/{idealTypeId}", 1L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer AccessToken")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(body)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(document("idealType/update",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("AccessToken")
                        ),
                        pathParameters(
                                parameterWithName("idealTypeId").description("IdealType Id ")
                        ),
                        requestFields(
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역(필수)"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별(필수) [여성/남성/무관]"),
                                fieldWithPath("age1").type(JsonFieldType.NUMBER).description("나이(필수)"),
                                fieldWithPath("age2").type(JsonFieldType.NUMBER).description("나이(필수)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("원하는 상대 Id"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("gender").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("age1").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("age2").type(JsonFieldType.NUMBER).description("나이")
                        )));

        then(idealTypeService).should(times(1)).updateIdealType(any(), any());
    }


}