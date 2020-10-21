package de.dealog.msg.rest;

import com.google.common.collect.Lists;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.service.RegionService;
import de.dealog.msg.service.model.QueryParams;
import de.dealog.msg.service.model.RegionalCode;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


/**
 * REST Resource for {@link RegionRest}s
 */
@Produces("application/de.dealog.service.regions-" + RegionResource.API_VERSION)
@Path(RegionResource.RESOURCE_PATH)
public class RegionResource {

    /**
     * Current API version
     */
    public static final String API_VERSION = "v1.0+json";

    /**
     * Regions path
     */
    public static final String RESOURCE_PATH = "/api/regions";

    /**
     * URI template parameter for ars
     */
    public static final String PATH_ARS = "ars";

    /**
     * URI path for hierarchy
     */
    private static final String PATH_HIERACHY ="hierarchy" ;

    /**
     * Query parameter for name
     */
    private static final String QUERY_NAME = "name";

    @Inject
    RegionConverter regionConverter;

    @Inject
    RegionalCodeConverter regionalCodeConverter;

    @Inject
    RegionService regionService;

    /**
     * Returns the region hierachy of an ARS
     *
     * @param ars an ars as string
     * @param geoRequest a {@link GeoRequest} containing the long and lat parameter
     * @param pageRequest a {@link PageRequest} containing the paging parameter
     * @return the hierachy list of {@link RegionRest}s,
     *          else {@link Response.Status#NOT_FOUND} or {@link Response.Status#BAD_REQUEST}
     */
    @GET
    @Path(PATH_HIERACHY)
    public Response findHierachy(
            @QueryParam(PATH_ARS) final String ars,
            @BeanParam final GeoRequest geoRequest,
            @BeanParam final PageRequest pageRequest) {
        final Response build;
        RegionalCode regionalCode = null;
        if (ars != null) {
            regionalCode = regionalCodeConverter.doForward(ars);
        }
        final QueryParams queryparams = QueryParams.builder()
                .point(geoRequest.getPoint())
                .regionalCode(regionalCode)
                .build();

        if (queryparams.maybeRegionalCode().isPresent() || queryparams.maybePoint().isPresent()) {
            final PagedList<? extends Region> regions = regionService.findHierachy(
                    queryparams, pageRequest.getPage(), pageRequest.getSize());
            final Iterable<RegionRest> regionRestIterator = regionConverter.convertAll(regions.getContent());

            final ArrayList<RegionRest> regionRests = Lists.newArrayList(regionRestIterator);
            regionRests.sort(Comparator.comparingInt(a -> a.getType().ordinal()));
            build = Response.ok(regionRests).build();
        } else {
            build = Response.status(Response.Status.BAD_REQUEST).build();
        }
        return build;
    }

    /**
     * Returns a paged list of regions. Filtered by name
     *
     * @param name the name of the regions to find.
     * @param pageRequest a {@link PageRequest} containing the paging parameter
     * @return a paged list of {@link RegionRest}s
     */
    @GET
    public Response findAll(
            @QueryParam(QUERY_NAME) final String name,
            @BeanParam final PageRequest pageRequest) {

        final PagedList<? extends Region> regions = regionService.findAll(
                name, pageRequest.getPage(), pageRequest.getSize());
        final Iterable<RegionRest> messagesRest = regionConverter.convertAll(regions.getContent());

        return Response.ok(messagesRest).build();
    }

    /**
     * Returns a {@link RegionRest} found by ars.
     *
     * @param ars the ars of the region to find.
     * @return if found the {@link Response.Status#OK} with {@link RegionRest}, else {@link Response.Status#NOT_FOUND}
     */
    @GET
    @Path("{" + PATH_ARS + "}")
    public Response find(@NotEmpty @PathParam(PATH_ARS) final String ars) {

        final Optional<Region> region = regionService.findOne(ars);
        final AtomicReference<Response> response = new AtomicReference<>();
        region.ifPresentOrElse(
                r -> response.set(Response.ok(regionConverter.convert(r)).build()),
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
