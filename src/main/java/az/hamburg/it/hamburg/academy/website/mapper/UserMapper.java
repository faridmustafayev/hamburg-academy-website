package az.hamburg.it.hamburg.academy.website.mapper;

import az.hamburg.it.hamburg.academy.website.dao.entity.UserEntity;
import az.hamburg.it.hamburg.academy.website.model.request.RegistrationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.UserResponse;

import static az.hamburg.it.hamburg.academy.website.model.enums.Role.ADMIN;

public enum UserMapper {
    USER_MAPPER;

    public UserEntity mapRegistrationRequestToEntity(RegistrationRequest request) {
        return UserEntity.builder()
                .username(request.getEmail())
                .password(request.getPassword())
                .role(ADMIN)
                .build();
    }

    public UserResponse mapEntityToResponse(UserEntity entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .email(entity.getUsername())
                .role(entity.getRole())
                .build();
    }
}