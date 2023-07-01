package ru.clevertec.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Authority enum, contains such roles as JOURNALIST(write and delete news),
 * SUBSCRIBER(write and delete comments) and ADMIN(write and delete both news and comments)
 *
 * @author Yuryeu Andrei
 * @see Authority
 */
@RequiredArgsConstructor
@Getter
public enum Role {

    JOURNALIST(List.of(
            Authority.WRITE_NEWS.getGrantedAuthority(),
            Authority.DELETE_NEWS.getGrantedAuthority())
    ),
    SUBSCRIBER(List.of(
            Authority.WRITE_COMMENTS.getGrantedAuthority(),
            Authority.DELETE_COMMENTS.getGrantedAuthority())
    ),
    ADMIN(List.of(
            Authority.WRITE_NEWS.getGrantedAuthority(),
            Authority.DELETE_NEWS.getGrantedAuthority(),
            Authority.WRITE_COMMENTS.getGrantedAuthority(),
            Authority.DELETE_COMMENTS.getGrantedAuthority())
    );

    private final List<GrantedAuthority> authorities;
}
