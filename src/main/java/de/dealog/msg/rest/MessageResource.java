package de.dealog.msg.rest;

import com.google.common.base.Converter;
import de.dealog.common.model.Status;
import de.dealog.msg.converter.MessageConverter;
import de.dealog.msg.converter.PagedListConverter;
import de.dealog.msg.messaging.tracking.MessageTrackingBroadcaster;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.rest.model.GeoRequest;
import de.dealog.msg.rest.model.MessageRest;
import de.dealog.msg.rest.model.PageRequest;
import de.dealog.msg.rest.model.PagedListRest;
import de.dealog.msg.rest.validations.ValidGeoRequest;
import de.dealog.msg.service.MessageService;
import de.dealog.msg.service.model.PagedList;
import de.dealog.msg.service.model.QueryParams;
import io.vertx.core.json.JsonArray;
import io.vertx.mutiny.core.eventbus.EventBus;

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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static de.dealog.msg.rest.model.RegionRequest.QUERY_ARS;

/**
 * REST Resource for {@link MessageRest}s
 */
@Produces("application/vnd.de.dealog.service.message-" + ResourceConstants.API_VERSION)
@Path(MessageConstants.RESOURCE_PATH)
public class MessageResource {

    private final MessageConverter messageConverter;

    private final MessageService messageService;

    private final EventBus eventBus;

    private PagedListConverter<Message, MessageRest> pagedListConverter;

    @Inject
    public MessageResource(final MessageConverter messageConverter,
                          final MessageService messageService, final EventBus eventBus) {
        this.messageConverter = messageConverter;
        this.messageService = messageService;
        this.eventBus = eventBus;

        this.pagedListConverter = new PagedListConverter<>() {
            @Override
            public void setContentConverter(final Converter<Message, MessageRest> messageConverter) {
                super.setContentConverter(messageConverter);
            }
        };
        this.pagedListConverter.setContentConverter(messageConverter);
    }
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

        if(messages.getContent().size() > 0) {
            eventBus.sendAndForget(MessageTrackingBroadcaster.MESSAGE_LIST_REQUEST,
                    new JsonArray(messages.getContent().stream().map(Message::getIdentifier).collect(Collectors.toList())));
        }
        final PagedListRest<MessageRest> response = pagedListConverter.convert(messages);
        return Response.ok(response).build();
    }

    /**
     * Returns a {@link MessageRest} found by identifier
     *
     * @param identifier the identifier of the message to find.
     * @return if found the {@link Response.Status#OK} with {@link MessageRest}, else {@link Response.Status#NOT_FOUND}
     */
    @GET
    @Path("{" + MessageConstants.PATH__PARAM_IDENTIFIER + "}")
    public Response find(@NotEmpty @PathParam(MessageConstants.PATH__PARAM_IDENTIFIER) final String identifier) {

        final Optional<Message> message = messageService.findOne(identifier, Status.Published);
        final AtomicReference<Response> response = new AtomicReference<>();
        message.ifPresentOrElse(
                m -> {
                    eventBus.sendAndForget(MessageTrackingBroadcaster.MESSAGE_SINGLE_REQUEST, m.getIdentifier());
                    response.set(Response.ok(messageConverter.convert(m)).build());
                },
                () -> response.set(Response.status(Response.Status.NOT_FOUND).build()));
        return response.get();
    }
}
