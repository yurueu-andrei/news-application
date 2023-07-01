package ru.clevertec.security.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Authority enum, contains such authorities as WRITE_COMMENTS, DELETE_COMMENTS, WRITE_NEWS, DELETE_NEWS
 *
 * @author Yuryeu Andrei
 */
@Getter
@RequiredArgsConstructor
public enum Authority {

    WRITE_COMMENTS(new SimpleGrantedAuthority("WRITE_COMMENTS")),
    DELETE_COMMENTS(new SimpleGrantedAuthority("DELETE_COMMENTS")),
    WRITE_NEWS(new SimpleGrantedAuthority("WRITE_NEWS")),
    DELETE_NEWS(new SimpleGrantedAuthority("DELETE_NEWS"));

    private final GrantedAuthority grantedAuthority;
}
