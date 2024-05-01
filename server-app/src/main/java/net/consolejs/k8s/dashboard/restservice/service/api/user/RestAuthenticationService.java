package net.consolejs.k8s.dashboard.restservice.service.api.user;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/auth")
public interface RestAuthenticationService {

    @Path("/")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response login(String body);

    @Path("/refresh")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getRefresh(@HeaderParam("RefreshToken") String refreshToken);
}
