package de.dealog.msg.service;

import de.dealog.msg.TestUtils;
import de.dealog.msg.persistence.Message;
import de.dealog.msg.persistence.MessageRepository;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class MessageServiceTest {

    public static final int PAGE_SIZE_3 = 3;

    public static final long TOTAL_COUNT = Long.MAX_VALUE;
    public static final int PAGE_NUMBER_ONE = 1;

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
}