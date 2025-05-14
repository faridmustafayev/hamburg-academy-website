package az.hamburg.it.hamburg.academy.website.service.concrete;

import az.hamburg.it.hamburg.academy.website.dao.repository.UserRepository;
import az.hamburg.it.hamburg.academy.website.model.enums.Role;
import az.hamburg.it.hamburg.academy.website.model.userDetails.CustomGrantedAuthority;
import az.hamburg.it.hamburg.academy.website.model.userDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static az.hamburg.it.hamburg.academy.website.exception.ExceptionConstants.USER_NOT_FOUND;
import static lombok.AccessLevel.PRIVATE;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CustomUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        var customGrantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new CustomGrantedAuthority(Role.valueOf(authority.getAuthority())))
                .collect(Collectors.toSet());

        return new CustomUserDetails(user, customGrantedAuthorities);
    }

}
