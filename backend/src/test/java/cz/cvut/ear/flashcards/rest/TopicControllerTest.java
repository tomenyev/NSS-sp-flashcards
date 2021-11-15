package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.service.TopicService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TopicControllerTest extends BaseControllerTestRunner {

    @InjectMocks
    private TopicController topicControllerMock;

    @Mock
    private TopicService topicServiceMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(topicControllerMock);
    }

    @Test
    public void addTopic() throws Exception {
        final Topic topic = Generator.generateTopic();

        Mockito.when(topicServiceMock.addTopic(Mockito.any(Topic.class))).thenReturn(topic);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/topics/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(topic)).accept(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        final Topic result = readValue(mvcResult, Topic.class);

        assertNotNull(result);
        assertEquals(result.getId(), result.getId());
        assertEquals(topic.getAuthor(), result.getAuthor());
    }

    @Test
    public void deleteTopic() throws Exception {
        mockMvc.perform(delete("/api/topics/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
