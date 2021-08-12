package org.inu.universe;

import org.inu.universe.domain.Account;
import org.inu.universe.domain.Email;
import org.inu.universe.domain.ProfileImage;
import org.inu.universe.domain.status.AccountRole;
import org.inu.universe.domain.status.AccountStatus;
import org.inu.universe.domain.status.EmailStatus;
import org.inu.universe.model.account.AccountLoginRequest;
import org.inu.universe.model.account.AccountSaveRequest;
import org.inu.universe.model.email.EmailAuthRequest;
import org.inu.universe.model.email.EmailSaveRequest;
import org.inu.universe.model.hashtag.HashTagResponse;
import org.inu.universe.model.hashtag.HashTagSaveRequest;
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

    public static final EmailSaveRequest EMAIL_SAVE_REQUEST
            = new EmailSaveRequest("a@inu.ac.kr");

    public static final EmailAuthRequest EMAIL_AUTH_REQUEST
            = new EmailAuthRequest("123456");

    public static final Email EMAIL
            = new Email(1L, "a@inu.ac.kr", EmailStatus.AUTH);

    public static final Email EMAIL_2
            = new Email(1L, "a@inu.ac.kr", EmailStatus.NOTAUTH);

    public static final Account ACCOUNT
            = new Account(1L,  "a", null, AccountStatus.ACTIVE, AccountRole.ROLE_USER, EMAIL,null);

    public static final Account ACCOUNT_2
            = new Account(1L,  "a", "abcd", AccountStatus.ACTIVE, AccountRole.ROLE_USER, EMAIL, null);

    public static final TokenDto TOKEN_DTO
            = new TokenDto("abcdefg", "ABCDEFG");

    public static final String REISSUE_ACCESS_TOKEN ="hijklmn";

    public static final ProfileSaveRequest PROFILE_SAVE_REQUEST
            = new ProfileSaveRequest("a", 20, "성별", "학과");

    public static final ProfileResponse PROFILE_RESPONSE
            = new ProfileResponse(1L, null, "a", 20, "성별", "학과", false, false,
            null, null, null, null, null, new ArrayList<>());

    public static final HashTagSaveRequest HASH_TAG_SAVE_REQUEST
            = new HashTagSaveRequest(asList("#만화", "#취준", "#운동"));

    public static final HashTagResponse HASH_TAG_RESPONSE_1
            = new HashTagResponse(1L, "#만화");

    public static final HashTagResponse HASH_TAG_RESPONSE_2
            = new HashTagResponse(2L, "#취준");

    public static final HashTagResponse HASH_TAG_RESPONSE_3
            = new HashTagResponse(3L, "#운동");

    public static final ProfileUpdateRequest PROFILE_UPDATE_REQUEST
            = new ProfileUpdateRequest("ab", 21, "성별", "학과", true, true, "지역", "키", "체형",
            "MBTI", "소개", asList("#만화", "#취준", "#운동"));

    public static final ProfileImage PROFILE_IMAGE
            = new ProfileImage("저장된 이름", "이미지 URL", "썸네일 이미지 URL");

    public static final ProfileResponse PROFILE_RESPONSE_2
            = new ProfileResponse(1L, PROFILE_IMAGE, "ab", 21, "성별", "학과", true, true, "지역", "키",
            "체형", "MBTI", "소개", asList("#만화", "#취준", "#운동"));


}
