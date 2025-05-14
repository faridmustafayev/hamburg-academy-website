package az.hamburg.it.hamburg.academy.website.service.abstraction;

import az.hamburg.it.hamburg.academy.website.model.request.RegistrationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.UserResponse;

public interface UserService {
    UserResponse getUserByEmail(String email);

    void registerUser(RegistrationRequest request);
}