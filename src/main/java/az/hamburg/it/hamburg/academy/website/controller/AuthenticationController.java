package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.jwt.RefreshTokenRequest;
import az.hamburg.it.hamburg.academy.website.model.request.LoginRequest;
import az.hamburg.it.hamburg.academy.website.model.response.LoginResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    @ResponseStatus(OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/refresh")
    @ResponseStatus(OK)
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authenticationService.refresh(request);
    }
}