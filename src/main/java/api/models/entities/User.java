package api.models.entities;

import api.configs.web.deserializers.InstantJsonDeserializer;
import api.configs.web.serializers.InstantJsonSerializer;
import api.models.enums.UserRole;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

import static api.models.enums.UserRole.ADMIN;
import static api.models.enums.UserRole.USER;

@Data
@With
@Builder
@Table("Users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    private Integer id;

    @NotEmpty(message = "The 'username' cannot be empty or null")
    private String username;

    @NotEmpty(message = "The 'password' cannot be empty or null")
    private String password;

    @Builder.Default
    @NotNull(message = "The 'role' cannot be empty or null")
    @Column("role")
    private UserRole role = USER;

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

    @Column("created_at")
    @CreatedDate
    @JsonDeserialize(using = InstantJsonDeserializer.class)
    @JsonSerialize(using = InstantJsonSerializer.class)
    private Instant createdAt;

    @Column("updated_at")
    @LastModifiedDate
    @JsonDeserialize(using = InstantJsonDeserializer.class)
    @JsonSerialize(using = InstantJsonSerializer.class)
    private Instant updatedAt;

    @Hidden
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return (switch (role) {
            case ADMIN -> Stream.of(ADMIN, USER);
            case USER -> Stream.of(USER);
        }).map(UserRole::getRole)
                .map(SimpleGrantedAuthority::new)
                .toList();
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
