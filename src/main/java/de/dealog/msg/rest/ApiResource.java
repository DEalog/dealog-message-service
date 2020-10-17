package de.dealog.msg.rest;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.validation.constraints.NotEmpty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * REST Resource for API version support validation
 */
@Path(ApiResource.RESOURCE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class ApiResource {
    public static final String RESOURCE_PATH = "/api/unsupported";
    public static final String QUERY_PARAM_VERSION = "version";

    @ConfigProperty(name = "dealog.api.version.unsupported")
    Optional<List<String>> mayBeVersions;

    /**
     * Request the supported status of a ....
     * @param version API version and ...
     * @return if the version is supported it returns {@link javax.ws.rs.core.Response.Status#NOT_FOUND},
*              else it return {@link javax.ws.rs.core.Response.Status#NO_CONTENT}
     */
    @GET
    public Response unsupported(@NotEmpty @QueryParam(QUERY_PARAM_VERSION) final String version) {

        Response response = Response.status(Response.Status.NOT_FOUND).build();

        if (mayBeVersions.isPresent() && mayBeVersions.get().contains(version)) {
            response = Response.noContent().build();
        }

        return response;
    }
}
