package net.consolejs.dashboard.restclient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import net.consolejs.dashboard.restfactory.KubernetesHeaderFactory;
import net.consolejs.dashboard.restmodel.KubernetesNamespace;
import net.consolejs.dashboard.restmodel.KubernetesNamespaces;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/api/v1/")
@RegisterRestClient(configKey="kubernetes-api")
@RegisterClientHeaders(KubernetesHeaderFactory.class)
public interface KubernetesService {

    @GET
    @Path("/namespaces")
    KubernetesNamespaces getNamespaces();
}

