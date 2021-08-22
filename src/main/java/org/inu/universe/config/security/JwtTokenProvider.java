package org.inu.universe.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.Base64;
import java.util.Date;

    /*
    Jwt 구조
    * Header : Signature를 해싱하기 위한 알고리즘 정보
    * Payload : Claim으로 구성되어 있음, 서버와 클라이언트 시스템에서 실제로 사용되는 정보에 대한 내용
    * Signature : Token의 유효성 검증을 위한 암호화된 문자열(-> 서버에서 유효한 Token인지 검증), secretKey를 포함하여 암호화
     */

/*
토큰 발급 및 검증
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    @Value("${spring.jwt.secretRefreshKey}")
    private String secretRefreshKey;

    private Long accessTokenValidTime = 1000L * 60 * 60;             // accessToken 유효 시간 ( 1시간 ) / [탈취당한 토큰의 무효화를 위해 짧음]
    private Long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7;  // refreshToken 유효 시간 ( 24시간 * 7 )

    private final UserDetailsService userDetailsService;       // 계정의 정보(인증된 객체) DB에서 가져옴. - AccountDetailsService

    // 스프링 빈 초기화 후 실행됨
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());     // secretKey 암호화
        secretRefreshKey = Base64.getEncoder().encodeToString(secretRefreshKey.getBytes());
    }

    /*
    Access_JWT 발급
     */
    public String createAccessToken(String accountPk) {

        Claims claims = Jwts.claims().setSubject(accountPk);     // Claims - 계정의 Id를 담아줌
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)     // payload 설정
                .setIssuedAt(now)      // accessToken의 발행 시간
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))    // accessToken의 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)     // secretKey 암호화
                .compact();
    }

    /*
    Refresh_JWT 발급
     */
    public String createRefreshToken(String accountPk) {

        Claims claims = Jwts.claims().setSubject(accountPk);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretRefreshKey)
                .compact();
    }


    /*
    JWT 토큰 검증, 유효성 검사
   - SecretKey를 통해 JWT 파싱 후, 만약 서버가 갖고 있는 SecretKey로 파싱이 되지 않는다면, 변조된 혹은 다른 서버의 JWT이므로 예외 발생
   - JWT 토큰의 유효 시간과 서버 시간을 비교하여 유효 시간이 좀 더 과거라면 FALSE 리턴 ( 토큰이 만료가 되었음을 의미 )
    */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);  // payload
            return !claims.getBody().getExpiration().before(new Date());  // 지금 시간보다 유효 시간이 과거인지 검증(과거이면 true) -> false ( 시간이 유효한 지 검증 )
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretRefreshKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /*
    인증 객체 반환
    - UserDetailsService -> AccountDetailsService
     */
    public Authentication getAuthentication(String jwtToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getAccountPk(jwtToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /*
    Token 발행 시 필요한 accountPk 발생 ( 계정의 Id )
    - 서버가 가지고 있는 SecretKey를 사용하여 JWT 파싱, 계정 Id 추출
     */
    public String getAccountPk(String jwtToken) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
                .getBody().getSubject();
    }
}
