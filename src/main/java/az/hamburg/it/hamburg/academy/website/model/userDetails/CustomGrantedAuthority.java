package az.hamburg.it.hamburg.academy.website.model.userDetails;

import az.hamburg.it.hamburg.academy.website.model.enums.Role;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CustomGrantedAuthority implements GrantedAuthority {
    String authority;

    public CustomGrantedAuthority(Role role) {
        this.authority = role.name();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}