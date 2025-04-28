package az.hamburg.it.hamburg.academy.website.controller;

import az.hamburg.it.hamburg.academy.website.model.request.RegistrationRequest;
import az.hamburg.it.hamburg.academy.website.service.abstraction.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void register(@RequestBody @Valid RegistrationRequest request) {
        userService.registerUser(request);
    }
}