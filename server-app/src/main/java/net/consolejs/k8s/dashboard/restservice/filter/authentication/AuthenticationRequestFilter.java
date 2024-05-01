package net.consolejs.k8s.dashboard.restservice.filter.authentication;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Response;
import net.consolejs.k8s.dashboard.utilities.JwtUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.lang.reflect.Method;
import java.util.Optional;

public class AuthenticationRequestFilter {
    private final String myJwtSecret;

    @Inject
    private ResourceInfo resourceInfo;

    public AuthenticationRequestFilter(@ConfigProperty(name = "JWT_SECRET") String jtwSecret) {
        myJwtSecret = jtwSecret;
    }

    @ServerRequestFilter()
    public Optional<Response> preMatchingFilter(ContainerRequestContext requestContext) {
        Method method = resourceInfo.getResourceMethod();

        if (method.isAnnotationPresent(Authentication.class) && !isAccessTokenValid(requestContext)) {
            return Optional.of(Response.status(Response.Status.UNAUTHORIZED)
                                       .build());
        }
        return Optional.empty();
    }

    private boolean isAccessTokenValid(ContainerRequestContext containerRequestContext) {
        String header = containerRequestContext.getHeaderString(Authentication.AUTH_HEADER);
        String token = JwtUtils.getTokenFromAuthHeader(header);
        if (token == null) {
            return false;
        }

        return JwtUtils.getIdIfValid(token, myJwtSecret) != null;
    }
}
