package ru.clevertec.news.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthority> {

    @Override
    public SecurityContext createSecurityContext(WithAuthority customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetails userDetails = User.builder()
                .username("mockUser")
                .password("mockPassword")
                .authorities(Arrays.stream(customUser.authorities())
                        .map(SimpleGrantedAuthority::new)
                        .toList()
                ).build();

        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }

}
