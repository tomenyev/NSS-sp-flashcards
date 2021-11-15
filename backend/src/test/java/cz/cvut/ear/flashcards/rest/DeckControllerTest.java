package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.service.CardService;
import cz.cvut.ear.flashcards.service.DeckService;
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
import static org.mockito.Mockito.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class DeckControllerTest extends BaseControllerTestRunner {

    @Mock
    private DeckService deckServiceMock;

    @Mock
    private CardService cardServiceMock;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private DeckController deckControllerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(deckControllerMock);
    }

    @Test
    public void addDeck_isSuccess() throws Exception {

        Deck deck = Generator.generateDeck();
        Topic topic = Generator.generateTopic();

        Mockito.when(deckServiceMock.addDeck(anyInt(), Mockito.any(Deck.class))).thenReturn(deck);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/decks/?topicId=" + topic.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(deck)).accept(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        final Deck result = readValue(mvcResult, Deck.class);

        assertNotNull(result);
        assertEquals(deck.getId(), result.getId());
        assertEquals(deck.getName(), result.getName());
    }


    @Test
    public void deleteDeck_isSuccess() throws Exception {
        mockMvc.perform(delete("/api/decks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/api/cards/1")).andExpect(status().isNotFound()).andDo(print());
    }

}
