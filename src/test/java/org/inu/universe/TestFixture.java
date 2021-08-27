package org.inu.universe;

import org.inu.universe.domain.Email;
import org.inu.universe.domain.ProfileImage;
import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountResponse;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.email.EmailRequest;
import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.model.hashtag.HashTagSaveRequest;
import org.inu.universe.model.idealType.IdealTypeRequest;
import org.inu.universe.model.idealType.IdealTypeResponse;
import org.inu.universe.model.profile.ProfileDto;
import org.inu.universe.model.profile.ProfileResponse;
import org.inu.universe.model.profile.ProfileSaveRequest;
import org.inu.universe.model.profile.ProfileUpdateRequest;
import org.inu.universe.model.token.TokenDto;

import java.util.ArrayList;

import static java.util.Arrays.*;

public class TestFixture {

    public static final AccountSaveRequest ACCOUNT_SAVE_REQUEST
            = new AccountSaveRequest("a@inu.ac.kr", "a", "a");

    public static final AccountLoginRequest ACCOUNT_LOGIN_REQUEST
            = new AccountLoginRequest("a@inu.ac.kr", "a");

    public static final EmailRequest EMAIL_REQUEST
            = new EmailRequest("a@inu.ac.kr");

    public static final EmailSaveRequest EMAIL_SAVE_REQUEST
            = new EmailSaveRequest("a@inu.ac.kr", "123456");

    public static final Email EMAIL
            = new Email(1L, "a@inu.ac.kr");

    public static final TokenDto TOKEN_DTO
            = new TokenDto("abcdefg", "ABCDEFG");

    public static final String REISSUE_ACCESS_TOKEN ="hijklmn";

    public static final ProfileSaveRequest PROFILE_SAVE_REQUEST
            = new ProfileSaveRequest("a", 20, "성별", "단과대", "학과");


    public static final HashTagSaveRequest HASH_TAG_SAVE_REQUEST
            = new HashTagSaveRequest(asList("#만화", "#취준", "#운동"));

    public static final HashTagResponse HASH_TAG_RESPONSE_1
            = new HashTagResponse(1L, "#만화");

    public static final HashTagResponse HASH_TAG_RESPONSE_2
            = new HashTagResponse(2L, "#취준");

    public static final HashTagResponse HASH_TAG_RESPONSE_3
            = new HashTagResponse(3L, "#운동");

    public static final ProfileUpdateRequest PROFILE_UPDATE_REQUEST
            = new ProfileUpdateRequest("ab", 21, "성별","단과대", "학과", "지역", "키", "체형",
            "MBTI", "소개", asList("#만화", "#취준", "#운동"), false);

    public static final ProfileImage PROFILE_IMAGE
            = new ProfileImage("저장된 이름", "이미지 URL", "썸네일 이미지 URL");

    public static final ProfileResponse PROFILE_RESPONSE
            = new ProfileResponse(1L, null, "a", 20, "성별", "단과대", "학과",
            null, null, null, null, null, new ArrayList<>(), false);

    public static final ProfileResponse PROFILE_RESPONSE_2
            = new ProfileResponse(1L, PROFILE_IMAGE, "ab", 21, "성별", "단과대", "학과", "지역", "키",
            "체형", "MBTI", "소개", asList("#만화", "#취준", "#운동"), false);

    public static final ProfileResponse PROFILE_RESPONSE_3
            = new ProfileResponse(1L, null, "a", 20, "성별", "단과대", "학과",
            null, null, null, null, null, new ArrayList<>(), true);

    public static final IdealTypeRequest IDEAL_TYPE_REQUEST
            = new IdealTypeRequest("지역", "성별", 20, 20);

    public static final IdealTypeResponse IDEAL_TYPE_RESPONSE
            = new IdealTypeResponse(1L, "지역", "성별", 20, 20);

    public static final IdealTypeRequest IDEAL_TYPE_REQUEST_2
            = new IdealTypeRequest("지역.", "성별.", 21, 21);

    public static final IdealTypeResponse IDEAL_TYPE_RESPONSE_2
            = new IdealTypeResponse(1L, "지역.", "성별.", 21, 21);

    public static final AccountResponse ACCOUNT_RESPONSE
            = new AccountResponse("1", "1", "1");

    public static final ProfileDto PROFILE_DTO
            = new ProfileDto(1L, PROFILE_IMAGE, "닉네임", 20, "성별", "학과");

    public static final ProfileDto PROFILE_DTO_2
            = new ProfileDto(2L, PROFILE_IMAGE, "닉네임", 20, "성별", "학과");

    public static final ProfileDto PROFILE_DTO_3
            = new ProfileDto(3L, PROFILE_IMAGE, "닉네임", 20, "성별", "학과");

    public static final ProfileDto PROFILE_DTO_4
            = new ProfileDto(4L, PROFILE_IMAGE, "닉네임", 20, "성별", "학과");

    public static final ProfileDto PROFILE_DTO_5
            = new ProfileDto(5L, PROFILE_IMAGE, "닉네임", 20, "성별", "학과");
}
