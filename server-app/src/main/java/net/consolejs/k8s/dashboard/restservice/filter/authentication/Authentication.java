package net.consolejs.k8s.dashboard.restservice.filter.authentication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
    static final String AUTH_HEADER = "Authorization";
}
