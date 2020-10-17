package de.dealog.msg.rest;

import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.MessageRest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedList;
import de.dealog.msg.service.MessageService;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * REST Resource for {@link Message}s
 */
@Produces("application/de.dealog.service.message-" + MessageResource.API_VERSION)
@Path(MessageResource.RESOURCE_PATH)
public class MessageResource {

    public static final String RESOURCE_PATH = "/api/messages";
    public static final String API_VERSION = "v1.0+json";
    public static final String PATH_IDENTIFIER = "identifier";

    @Inject
    MessageConverter messageConverter;

    @Inject
    MessageService messageService;

    /**
     * Request {@link Message}s limited by...
     * @param pageRequest the page request page and page size
     * @return the list
     */
    @GET
    public Response findAll(@BeanParam final GeoRequest geoRequest, @BeanParam final PageRequest pageRequest) {

        final PagedList<? extends Message> messages = messageService.find(
                geoRequest.getPoint(), pageRequest.getPage(), pageRequest.getSize());
        final Iterable<MessageRest> messagesRest = messageConverter.convertAll(messages.getContent());

        return Response.ok(messagesRest).build();
    }

    /**
     * Request a {@link Message} by...
     * @param identifier its unique identifier
     * @return the message or {@link Response.Status#NOT_FOUND}
     */
    @GET
    @Path("{" + PATH_IDENTIFIER + "}")
    public Response find(@PathParam(PATH_IDENTIFIER) final String identifier) {

        final Optional<Message> message = messageService.find(identifier, MessageStatus.Published);
        final AtomicReference<Response> response = new AtomicReference<>();
        message.ifPresentOrElse(
                m -> response.set(Response.ok(messageConverter.convert(m)).build()),
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
