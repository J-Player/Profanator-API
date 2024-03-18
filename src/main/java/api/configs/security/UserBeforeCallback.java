package api.configs.security;

import api.models.entities.User;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserBeforeCallback implements BeforeConvertCallback<User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Publisher<User> onBeforeConvert(User entity, SqlIdentifier table) {
        return Mono.just(entity)
                .doOnNext(user -> passwordEncoder.upgradeEncoding(user.getPassword()))
                .onErrorResume(IllegalArgumentException.class, e -> Mono.just(entity
                        .withPassword(passwordEncoder.encode(entity.getPassword()))));
    }

}
