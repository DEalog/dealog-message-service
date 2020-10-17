package de.dealog.msg.service;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.model.Message;
import de.dealog.msg.persistence.model.MessageEntity;
import de.dealog.msg.persistence.model.MessageStatus;
import de.dealog.msg.persistence.repository.GeocodeRepository;
import de.dealog.msg.persistence.repository.MessageRepository;
import de.dealog.msg.rest.model.PagedList;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.geolatte.geom.G2D;
import org.geolatte.geom.Geometries;
import org.geolatte.geom.Point;
import org.geolatte.geom.crs.CoordinateReferenceSystems;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class MessageServiceTest {

    public static final int PAGE_NUMBER_ONE = 1;
    public static final int PAGE_SIZE_3 = 3;
    public static final long TOTAL_COUNT = Long.MAX_VALUE;

    @Inject
    MessageService messageService;

    @InjectMock
    MessageRepository messageRepository;

    @InjectMock
    GeocodeRepository geocodeRepository;

    @Test
    void findOneMessage() {
        final MessageEntity persisted = new MessageEntity();
        final String identifier = "eac3c557-eb9c-472c-8593-cc49098867f1";
        persisted.setIdentifier(identifier);
        when(messageRepository.findByIdentifierAndStatus(identifier, MessageStatus.Published)).thenReturn(persisted);

        final Optional<Message> message = messageService.find(identifier, MessageStatus.Published);
        assertThat(message.isPresent(), notNullValue());
        assertThat(message.get().getIdentifier(), is(identifier));

        when(messageRepository.findByIdentifier("667")).thenReturn(null);
        final Optional<Message> doesNotExist = messageService.find("667", MessageStatus.Published);
        assertThat(doesNotExist.isPresent(), is(false));
    }

    @Test
    void findMessagesByPoint() {
        PanacheQuery query = Mockito.mock(PanacheQuery.class);
        PanacheQuery queryPage = Mockito.mock(PanacheQuery.class);
        when(queryPage.list()).thenReturn(Collections.emptyList());

        when(query.pageCount()).thenReturn(0);
        when(query.page(0, 1)).thenReturn(queryPage);
        when(query.count()).thenReturn(TOTAL_COUNT);
        when(messageRepository.find(anyString(), any(Sort.class), any(Parameters.class))).thenReturn(query);

        Point<G2D> g2DPoint = Geometries.mkPoint(new G2D(8, 9), CoordinateReferenceSystems.WGS84);
        messageService.find(g2DPoint, 0, 1);

        ArgumentCaptor<Parameters> parameterCaptor = ArgumentCaptor.forClass(Parameters.class);
        Mockito.verify(messageRepository, Mockito.times(1)).find(any(String.class), any(Sort.class), parameterCaptor.capture());
        Parameters parameters = parameterCaptor.getValue();
        assertThat(g2DPoint, is(parameters.map().get(MessageService.POINT)));
    }

    @Test
    void findMessages() {
        Message msg_one = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        Message msg_two = TestUtils.buildMessage("2", "This is the second headline", "This is the second description");
        Message msg_three = TestUtils.buildMessage("3", "This is the third headline", "This is the third description");
        List<Message> messages = Arrays.asList(msg_one, msg_two, msg_three);

        PanacheQuery query = Mockito.mock(PanacheQuery.class);
        PanacheQuery queryPage = Mockito.mock(PanacheQuery.class);
        when(queryPage.list()).thenReturn(messages);

        int pageCount = (int) (TOTAL_COUNT / PAGE_SIZE_3);
        when(query.pageCount()).thenReturn(pageCount);
        when(query.page(1, PAGE_SIZE_3)).thenReturn(queryPage);
        when(query.count()).thenReturn(TOTAL_COUNT);
        when(messageRepository.find(anyString(), any(Sort.class), any(Parameters.class))).thenReturn(query);

        PagedList<? extends Message> list = messageService.find(null, PAGE_NUMBER_ONE, PAGE_SIZE_3);

        assertThat(PAGE_NUMBER_ONE, is(list.getPage()));
        assertThat(messages.size(), is(list.getPageSize()));
        assertThat(TOTAL_COUNT, is(list.getCount()));
        assertThat(pageCount, is(list.getPageCount()));
    }

    @Test
    void updateMessage() {
        MessageEntity persisted = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        MessageEntity updated = TestUtils.buildMessage("1", "This is a new headline", "This is a new description");
        when(messageRepository.findByIdentifier("1")).thenReturn(persisted);
        when(geocodeRepository.findByHash(anyString())).thenReturn(null);
        messageService.update(updated);

        ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        Mockito.verify(messageRepository, Mockito.times(1)).persistAndFlush(messageCaptor.capture());
        MessageEntity capturedMessage = messageCaptor.getValue();
        assertThat(updated.getIdentifier(), is(capturedMessage.getIdentifier()));
        assertThat(updated.getHeadline(), is(capturedMessage.getHeadline()));
        assertThat(updated.getDescription(), is(capturedMessage.getDescription()));
    }

    @Test
    void updateMessageStatus() {
        MessageEntity persisted = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        persisted.setStatus(MessageStatus.Published);
        when(messageRepository.findByIdentifier("1")).thenReturn(persisted);

        Message superseded = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        messageService.updateStatus(superseded.getIdentifier(), MessageStatus.Superseded);

        ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        Mockito.verify(messageRepository, Mockito.times(1)).persistAndFlush(messageCaptor.capture());
        MessageEntity capturedMessage = messageCaptor.getValue();
        assertThat(MessageStatus.Superseded, is(capturedMessage.getStatus()));
    }
}