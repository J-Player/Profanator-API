package api.services.impl;

import static api.models.enums.TokenType.REFRESH_TOKEN;

import java.util.Collection;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.stereotype.Service;

import api.exceptions.CookieNotFoundException;
import api.exceptions.InvalidTokenException;
import api.mappers.UserMapper;
import api.models.entities.User;
import api.models.security.AuthenticationRequest;
import api.models.security.AuthenticationResponse;
import api.models.security.RegisterRequest;
import api.services.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ReactiveAuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    public Mono<Void> register(RegisterRequest registerRequest) {
        return userService.findByUsername(registerRequest.username())
                .switchIfEmpty(Mono.just(registerRequest)
                        .map(UserMapper.INSTANCE::toUser)
                        .flatMap(userService::save))
                .then();
    }

    public Mono<AuthenticationResponse> login(AuthenticationRequest authenticationRequest, ServerHttpResponse response) {
        String username = authenticationRequest.username();
        String password = authenticationRequest.password();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        return Mono.just(authenticationToken)
                .flatMap(authentication -> userService.findByName(authentication.getName()))
                .then(authenticationManager.authenticate(authenticationToken)
                        .map(Authentication::getPrincipal)
                        .cast(User.class)
                        .map(user -> {
                            ResponseCookie refreshToken = tokenService.generateRefreshToken(user);
                            response.getHeaders().add(HttpHeaders.SET_COOKIE, refreshToken.toString());
                            String accessToken = tokenService.generateAccessToken(user);
                            return new AuthenticationResponse(accessToken);
                        }));
    }

    public Mono<Void> logout(ServerHttpRequest request, ServerHttpResponse response) {
        return Mono.just(request)
                .map(ServerHttpRequest::getCookies)
                .flatMapMany(cookies -> Flux.fromIterable(cookies.values().stream()
                        .flatMap(Collection::stream)
                        .toList()))
                .doOnNext(cookie -> response.addCookie(ResponseCookie
                        .from(cookie.getName())
                        .path("/")
                        .maxAge(0)
                        .build()))
                .then();
    }

    public Mono<AuthenticationResponse> refresh(ServerHttpRequest request) {
        final HttpCookie cookie = request.getCookies().getFirst(REFRESH_TOKEN.getType());
        if (cookie == null)
            throw new CookieNotFoundException("\"%s\" cookie not found".formatted(REFRESH_TOKEN.getType()));
        final String refreshToken = cookie.getValue();
        if (refreshToken.length() == 0) throw new InvalidCookieException("Invalid cookie");
        final String username = tokenService.extractSubject(refreshToken);
        if (username == null) throw new InvalidTokenException();
        final String type = tokenService.extractType(refreshToken);
        if (type == null || !type.
                equals(REFRESH_TOKEN.getType())) throw new InvalidTokenException();
        return userService.findByName(username)
                .filter(userService::verifyUser)
                .filter(userDetails -> tokenService.isValidToken(refreshToken, userDetails))
                .map(user -> new AuthenticationResponse(tokenService.generateAccessToken(user)));
    }


    public static Mono<User> getPrincipal() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(User.class);
    }

}
