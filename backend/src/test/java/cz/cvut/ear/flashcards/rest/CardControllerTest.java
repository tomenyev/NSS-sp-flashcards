package cz.cvut.ear.flashcards.rest;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.service.CardService;
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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CardControllerTest extends BaseControllerTestRunner {

    @Mock
    private CardService cardServiceMock;

    @InjectMocks
    private CardController cardControllerMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(cardControllerMock);
    }

    @Test
    public void getCard_ifRightId() throws Exception {

        final Card card = Generator.generateCard();
        Mockito.when(cardServiceMock.getCard(card.getId())).thenReturn(card);

        final MvcResult mvcResult = mockMvc.perform(
                get("/api/cards/" + card.getId())).andReturn();

        final Card result = readValue(mvcResult, Card.class);

        assertNotNull(result);
        assertEquals(card.getId(), result.getId());
        assertEquals(card.getAnswer(), result.getAnswer());
    }

    @Test
    public void addCard_isSuccess() throws Exception {

        final Card card = Generator.generateCard();
        final Deck deck = Generator.generateDeck();

        Mockito.when(cardServiceMock.addCard(Mockito.any(Card.class), Mockito.anyInt())).thenReturn(card);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/cards/?deckId=" + deck.getId())
                .accept(MediaType.APPLICATION_JSON).content(toJson(card))
                .contentType(MediaType.APPLICATION_JSON);

        final MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        final Card result = readValue(mvcResult, Card.class);

        assertNotNull(result);
        assertEquals(card.getId(), result.getId());
        assertEquals(card.getAnswer(), result.getAnswer());
//        verifyLocationEquals("/api/cards/" + card.getId(), mvcResult);

    }

    @Test
    public void addCard_deckIdIsNotSet_cardDidNotAdded() throws Exception {
        final Card card = Generator.generateCard();

        Mockito.when(cardServiceMock.addCard(Mockito.any(Card.class), Mockito.notNull())).thenReturn(card);

        final RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/cards/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(card)).accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());

    }

    @Test
    public void deleteCard_isSuccess() throws Exception {

        final Card card = Generator.generateCard();

        mockMvc.perform(delete("/api/cards/" + card.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCard_isSuccess() throws Exception {
        final Card card = Generator.generateCard();
        final Deck deck = Generator.generateDeck();
        Card newCard = card;

        Mockito.when(cardServiceMock.getCard(card.getId())).thenReturn(card);

        newCard.setAnswer("newAnswer");
        newCard.setQuestion("newQuestion");

        mockMvc.perform(put("/api/cards/" + card.getId() + "?deckId=" + deck.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(newCard)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/cards/" + card.getId()))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(card.getId()))).andExpect(jsonPath("$.question", is("newQuestion")))
                .andExpect(jsonPath("$.answer", is("newAnswer")));
    }

}
