package hr.algebra.gamearena.webapp.models.cereal.authentication;

import com.gamearena.client.model.TokenDto;

public record TokenDecereal(String token) {
    public static TokenDecereal fromTokenDtoClient(TokenDto tokenDto) {
        return new TokenDecereal(tokenDto.getToken());
    }
}
