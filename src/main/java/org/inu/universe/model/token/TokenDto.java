package org.inu.universe.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;
    private String refreshToken;

    public static TokenDto from(String accessToken, String refreshToken) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.accessToken = accessToken;
        tokenDto.refreshToken = refreshToken;
        return tokenDto;
    }
}
