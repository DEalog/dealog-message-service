package de.dealog.msg.rest;

import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.MessageRest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.service.model.QueryParams;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * REST Resource for {@link MessageRest}s
 */
@Produces("application/de.dealog.service.message-" + MessageResource.API_VERSION)
@Path(MessageResource.RESOURCE_PATH)
public class MessageResource {

    /**
     * Current API version
     */
    public static final String API_VERSION = "v1.0+json";

    /**
     * URI template parameter for ars
     */
    public static final String RESOURCE_PATH = "/api/messages";

    /**
     * URI path parameter for messages
     */
    public static final String PATH_IDENTIFIER = "identifier";

    /**
     * URI template parameter for ars
     */
    public static final String QUERY_ARS = "ars";

    @Inject
    MessageConverter messageConverter;

    @Inject
    MessageService messageService;

    /**
     * Returns a paged list of {@link MessageRest}s. Can be filtered by {@link GeoRequest} or an ars
     *
     * @param ars the ars as string that should match the messages
     * @param geoRequest a {@link GeoRequest} containing the long and lat parameter
     * @param pageRequest a {@link PageRequest} containing the paging parameter
     * @return the found {@link MessageRest}s
     */
    @GET
    public Response findAll(
            @Pattern(regexp="(^[0-9]{2,12})") @Size(min = 2, max = 12) @QueryParam(QUERY_ARS) final String ars,
            @ValidGeoRequest @BeanParam final GeoRequest geoRequest,
            @BeanParam final PageRequest pageRequest) {

        final QueryParams queryparams = QueryParams.builder()
                .ars(ars)
                .point(geoRequest.getPoint())
                .build();
        final PagedList<? extends Message> messages = messageService.findAll(
                queryparams, pageRequest.getPage(), pageRequest.getSize());
        final Iterable<MessageRest> messagesRest = messageConverter.convertAll(messages.getContent());

        return Response.ok(messagesRest).build();
    }

    /**
     * Returns a {@link MessageRest} found by identifier
     *
     * @param identifier the identifier of the message to find.
     * @return if found the {@link Response.Status#OK} with {@link MessageRest}, else {@link Response.Status#NOT_FOUND}
     */
    @GET
    @Path("{" + PATH_IDENTIFIER + "}")
    public Response find(@NotEmpty @PathParam(PATH_IDENTIFIER) final String identifier) {

        final Optional<Message> message = messageService.findOne(identifier, MessageStatus.Published);
        final AtomicReference<Response> response = new AtomicReference<>();
        message.ifPresentOrElse(
                m -> response.set(Response.ok(messageConverter.convert(m)).build()),
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
