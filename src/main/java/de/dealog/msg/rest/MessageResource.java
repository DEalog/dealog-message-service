package de.dealog.msg.rest;

import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.rest.model.*;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.service.model.QueryParams;
import de.dealog.msg.service.model.RegionalCode;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * REST Resource for {@link MessageRest}s
 */
@Produces("application/vnd.de.dealog.service.message-" + MessageResource.API_VERSION)
@Path(MessageResource.RESOURCE_PATH)
public class MessageResource {

    public static final String RESOURCE_PATH = "/api/messages";
    public static final String API_VERSION = "v1.0+json";
    public static final String PATH_IDENTIFIER = "identifier";
    public static final String QUERY_ARS = "ars";

    @Inject
    MessageConverter messageConverter;

    @Inject
    RegionalCodeConverter regionalCodeConverter;

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
            @QueryParam(QUERY_ARS) final String ars,
            @BeanParam final GeoRequest geoRequest,
            @BeanParam final PageRequest pageRequest) {

        RegionalCode regionalCode = null;
        if (ars != null) {
            regionalCode = regionalCodeConverter.doForward(ars);
        }
        final QueryParams queryparams = QueryParams.builder()
                .point(geoRequest.getPoint())
                .regionalCode(regionalCode)
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
    public Response find(@PathParam(PATH_IDENTIFIER) final String identifier) {

        final Optional<Message> message = messageService.findOne(identifier, MessageStatus.Published);
        final AtomicReference<Response> response = new AtomicReference<>();
        message.ifPresentOrElse(
                m -> response.set(Response.ok(messageConverter.convert(m)).build()),
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
