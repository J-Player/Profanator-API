package api.domains;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("profanator_user")
public class User implements UserDetails {

    @Id
    private UUID id;
    @NotEmpty(message = "The 'username' cannot be empty or null")
    private String username;
    @NotEmpty(message = "The 'password' cannot be empty or null")
    private String password;
    @NotEmpty(message = "The 'authorities' cannot be empty or null")
    private String authorities;
    @Builder.Default
    @Column("accountNonLocked")
    private boolean accountNonLocked = true;
    @Builder.Default
    @Column("accountNonExpired")
    private boolean accountNonExpired = true;
    @Builder.Default
    @Column("credentialsNonExpired")
    private boolean credentialsNonExpired = true;
    @Builder.Default
    @Column("enabled")
    private boolean enabled = true;

    @Hidden
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}