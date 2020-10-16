package de.dealog.msg.rest;

import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.MessageRest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.rest.model.PagedList;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * REST Resource for {@link Message}s
 */
@Path(MessageResource.RESOURCE_PATH)
@Produces("application/de.dealog.service.message-" + MessageResource.API_VERSION)
public class MessageResource {

    public static final String RESOURCE_PATH = "/api/message";
    public static final String API_VERSION = "v1.0+json";

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
    public Response getAll(@BeanParam final GeoRequest geoRequest, @BeanParam final PageRequest pageRequest) {

        final PagedList<? extends Message> messages = messageService.list(
                geoRequest.getPoint(), pageRequest.getPage(), pageRequest.getSize());
        final Iterable<MessageRest> messagesRest = messageConverter.convertAll(messages.getContent());

        return Response.ok(messagesRest).build();
    }
}
