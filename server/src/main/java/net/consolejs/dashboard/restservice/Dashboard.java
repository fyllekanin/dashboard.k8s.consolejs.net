package net.consolejs.dashboard.restservice;

import net.consolejs.dashboard.restclient.KubernetesService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/configuration")
public class Dashboard {

    @Inject()
    @RestClient
    KubernetesService myKubernetesService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfiguration() {
        return Response.status(Response.Status.OK)
                .entity(myKubernetesService.getNamespaces())
                .build();
    }
}
