package de.dealog.msg.service;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageEntity;
import de.dealog.msg.persistence.MessageRepository;
import de.dealog.msg.persistence.MessageStatus;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void list() {
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

        PagedList<? extends Message> list = messageService.list(null, PAGE_NUMBER_ONE, PAGE_SIZE_3);
        Assertions.assertEquals(messages.size(), list.getContent().size());
        Assertions.assertEquals(PAGE_NUMBER_ONE, list.getPage());
        Assertions.assertEquals(messages.size(), list.getPageSize());
        Assertions.assertEquals(TOTAL_COUNT, list.getCount());
        Assertions.assertEquals(pageCount, list.getPageCount());
    }

    @Test
    void update() {
        MessageEntity persisted = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        Message updated = TestUtils.buildMessage("1", "This is a new headline", "This is a new description");
        when(messageRepository.findByIdentifier("1")).thenReturn(persisted);
        messageService.update(updated);

        ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        Mockito.verify(messageRepository, Mockito.times(1)).persistAndFlush(messageCaptor.capture());
        MessageEntity capturedMessage = messageCaptor.getValue();
        assertEquals(updated.getIdentifier(), capturedMessage.getIdentifier());
        assertEquals(updated.getHeadline(), capturedMessage.getHeadline());
        assertEquals(updated.getDescription(), capturedMessage.getDescription());
    }

    @Test
    void updateStatus() {
        MessageEntity persisted = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        persisted.setStatus(MessageStatus.Published);
        when(messageRepository.findByIdentifier("1")).thenReturn(persisted);

        Message superseded = TestUtils.buildMessage("1", "This is the headline", "This is the description");
        messageService.updateStatus(superseded.getIdentifier(), MessageStatus.Superseded);

        ArgumentCaptor<MessageEntity> messageCaptor = ArgumentCaptor.forClass(MessageEntity.class);
        Mockito.verify(messageRepository, Mockito.times(1)).persistAndFlush(messageCaptor.capture());
        MessageEntity capturedMessage = messageCaptor.getValue();
        assertEquals(MessageStatus.Superseded, capturedMessage.getStatus());
    }
}