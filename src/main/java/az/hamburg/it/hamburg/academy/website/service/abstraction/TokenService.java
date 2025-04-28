package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.jwt.AuthPayloadDto;
import az.hamburg.it.hamburg.academy.website.model.response.LoginResponse;

public interface TokenService {
    LoginResponse generateToken(AuthPayloadDto dto);

    LoginResponse refreshToken(String refreshToken);

    AuthPayloadDto validateToken(String accessToken);
}
