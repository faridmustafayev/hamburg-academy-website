package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.jwt.RefreshTokenRequest;
import az.hamburg.it.hamburg.academy.website.model.request.LoginRequest;
import az.hamburg.it.hamburg.academy.website.model.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request);

    LoginResponse refresh(RefreshTokenRequest request);
}