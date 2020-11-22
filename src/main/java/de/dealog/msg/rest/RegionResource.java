package de.dealog.msg.rest;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import de.dealog.msg.converter.PagedListConverter;
import de.dealog.msg.converter.RegionConverter;
import de.dealog.msg.converter.RegionalCodeConverter;
import de.dealog.msg.persistence.model.Region;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedListRest;
import de.dealog.msg.rest.model.RegionRest;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import de.dealog.msg.service.RegionService;
import de.dealog.msg.service.model.PagedList;
import de.dealog.msg.service.model.QueryParams;
import de.dealog.msg.service.model.RegionalCode;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


/**
 * REST Resource for {@link RegionRest}s
 */
@Produces("application/vnd.de.dealog.service.region-" + ResourceConstants.API_VERSION)
@Path(RegionResourceConstants.RESOURCE_PATH)
public class RegionResource {

    private final RegionConverter regionConverter;

    private final RegionalCodeConverter regionalCodeConverter;

    private final RegionService regionService;

    private PagedListConverter<Region, RegionRest> pagedListConverter;

    @Inject
    public RegionResource(final RegionConverter regionConverter,
                               final RegionalCodeConverter regionalCodeConverter, final RegionService regionService) {
        this.regionConverter = regionConverter;
        this.regionalCodeConverter = regionalCodeConverter;
        this.regionService = regionService;

        this.pagedListConverter = new PagedListConverter<>() {
            @Override
            public void setContentConverter(final Converter<Region, RegionRest> regionConverter) {
                super.setContentConverter(regionConverter);
            }
        };
        this.pagedListConverter.setContentConverter(regionConverter);
    }

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
    @Path(RegionResourceConstants.PATH_HIERACHY)
    public Response findHierachy(
            @Pattern(regexp="(^[0-9]{2,12})") @Size(min = 2, max = 12) @QueryParam(RegionResourceConstants.PATH_PARAM_ARS) final String ars,
            @ValidGeoRequest @BeanParam final GeoRequest geoRequest,
            @BeanParam final PageRequest pageRequest) {
        final Response build;
        RegionalCode regionalCode = null;
        if (StringUtils.isNotEmpty(ars)) {
            regionalCode = regionalCodeConverter.convert(ars);
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
            @Size(min = 3) @QueryParam(RegionResourceConstants.QUERY_NAME) final String name,
            @BeanParam final PageRequest pageRequest) {

        final PagedList<? extends Region> regions = regionService.findAll(
                name, pageRequest.getPage(), pageRequest.getSize());

        final PagedListRest<RegionRest> response = pagedListConverter.convert(regions);

        return Response.ok(response).build();
    }

    /**
     * Returns a {@link RegionRest} found by ars.
     *
     * @param ars the ars of the region to find.
     * @return if found the {@link Response.Status#OK} with {@link RegionRest}, else {@link Response.Status#NOT_FOUND}
     */
    @GET
    @Path("{" + RegionResourceConstants.PATH_PARAM_ARS + "}")
    public Response find(@NotEmpty @PathParam(RegionResourceConstants.PATH_PARAM_ARS) final String ars) {

        final Optional<Region> mayBeRegion = regionService.findOne(ars);
        final AtomicReference<Response> response = new AtomicReference<>();
        mayBeRegion.ifPresentOrElse(
                region -> response.set(Response.ok(regionConverter.convert(region)).build()),
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
