package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.annotation.Log;
import az.hamburg.it.hamburg.academy.website.annotation.LogIgnore;
import az.hamburg.it.hamburg.academy.website.dao.entity.UserEntity;
import az.hamburg.it.hamburg.academy.website.dao.repository.UserRepository;
import az.hamburg.it.hamburg.academy.website.exception.AlreadyExistsException;
import az.hamburg.it.hamburg.academy.website.exception.NotFoundException;
import az.hamburg.it.hamburg.academy.website.model.request.RegistrationRequest;
import az.hamburg.it.hamburg.academy.website.model.response.UserResponse;
import az.hamburg.it.hamburg.academy.website.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.USER_ALREADY_EXISTS;
import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.USER_NOT_FOUND;
import static az.hamburg.it.hamburg.academy.website.mapper.UserMapper.USER_MAPPER;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class UserServiceHandler implements UserService {
    PasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @LogIgnore
    @Override
    public UserResponse getUserByEmail(String email) {
        var user = fetchUserIfExist(email);
        return USER_MAPPER.mapEntityToResponse(user);
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUser(RegistrationRequest request) {
        if (checkIfExist(request.getEmail())) {
            throw new AlreadyExistsException(USER_ALREADY_EXISTS.getCode(), USER_ALREADY_EXISTS.getMessage());
        }

        if (!request.getRole().name().equals("ADMIN")) {
            throw new SecurityException("only admin can register");
        }

        var userEntity = USER_MAPPER.mapRegistrationRequestToEntity(request);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
    }

    @LogIgnore
    private UserEntity fetchUserIfExist(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND.getCode(), USER_NOT_FOUND.getMessage()));
    }

    @LogIgnore
    private boolean checkIfExist(String username) {
        return userRepository.existsByUsername(username);
    }

}