package ru.clevertec.security.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithAuthority {

    String[] authorities() default
            {"WRITE_COMMENTS", "WRITE_NEWS", "DELETE_COMMENTS", "DELETE_COMMENTS"}; //admin
}
