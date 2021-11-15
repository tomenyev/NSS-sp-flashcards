package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.dto.CardDto;
import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.model.Card;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.repository.CardRepository;
import cz.cvut.ear.flashcards.repository.DeckRepository;
import cz.cvut.ear.flashcards.repository.TopicRepository;
import cz.cvut.ear.flashcards.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CardServiceTest {
    @Autowired
    CardService cardService;
    @Autowired
    UserService userServiceImpl;
    @Autowired
    TopicService topicService;
    @Autowired
    DeckService deckService;
    @Autowired
    DeckRepository deckRepository;
    @Autowired
    TopicRepository topicRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void getCardTest() {
        Card card = Generator.generateCard();
        Deck deck = Generator.generateDeck();
        deck = deckRepository.save(deck);
        card = cardService.addCard(card, deck.getId());
        deck.addCard(card);
        deckRepository.save(deck);
        card = cardService.getCard(card.getId());
        assertNotNull(card);
    }

    @Test
    public void addCardTest() {
        Card card = Generator.generateCard();
        Deck deck = Generator.generateDeck();
        deck = deckRepository.save(deck);
        card = cardService.addCard(card, deck.getId());
        assertNotNull(card);
    }

    @Test
    public void updateCardTest() {
        Card card = Generator.generateCard();
        Deck deck = Generator.generateDeck();
        deck = deckRepository.save(deck);
        card = cardService.addCard(card, deck.getId());

        String s = card.getAnswer();
        card.setAnswer("newanswer2");

        cardService.updateCard(0, card.getId(), card);

        card = cardService.getCard(card.getId());

        assertEquals(card.getAnswer(), "newanswer2");
        assertNotSame(card.getAnswer(), s);
    }

    @Test(expected = NotFoundException.class)
    public void deleteCardTest() {

//        cardRepository.deleteAll();

        Card card = Generator.generateCard();
        Deck deck = Generator.generateDeck();
        deck = deckRepository.save(deck);
        card = cardService.addCard(card, deck.getId());

        cardService.deleteCard(card.getId());

        cardService.getCard(card.getId());
    }

    @Test
    public void getDeckCardsTest() {

        Set<Card> cards = new HashSet<>();
        Set<Card> get = new HashSet<>();

        Deck deck = Generator.generateDeck();
        deck = deckRepository.save(deck);

        for (int i = 0; i < 10; i++) {
            Card card = Generator.generateCard();
            card = cardRepository.save(card);
            deck.addCard(card);
            cards.add(card);
        }

        deckRepository.save(deck);

        get = cardService.getDeckCards(deck.getId());

        assertEquals(cards, get);
        assertEquals(10, get.size());
    }

//    @Test
//    public void getUserCardsTest() {
//        User user = Generator.generateUser();
//        user.setId(null);
//        user = userServiceImpl.addUser(user);
//        assertNotNull(user);
//
//        Topic topic = Generator.generateTopic();
//        Deck deck = Generator.generateDeck();
//        topic = topicRepository.save(topic);
//        deck = deckRepository.save(deck);
//        topic.addDeck(deck);
//        topic = topicRepository.save(topic);
//        user.addTopic(topic);
//        user = userRepository.save(user);
//
//        Set<Card> cards = new HashSet<>();
//        Set<Card> get = new HashSet<>();
//
//        for (int i = 0; i < 10; i++) {
//            Card card = Generator.generateCard();
//            card = cardRepository.save(card);
//            deck.addCard(card);
//            cards.add(card);
//        }
//
//        deckRepository.save(deck);
//
//        get = cardService.getUserCards(user.getId());
//
//        assertEquals(cards, get);
//        assertEquals(get.size(), 10);
//
//    }

    @Test
    public void getTopicCardsTest() {
        Topic topic = Generator.generateTopic();
        Deck deck = Generator.generateDeck();
        topic = topicRepository.save(topic);
        deck = deckRepository.save(deck);
        topic.addDeck(deck);
        topic = topicRepository.save(topic);

        Set<Card> cards = new HashSet<>();
        Set<Card> get = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Card card = Generator.generateCard();
            card = cardRepository.save(card);
            deck.addCard(card);
            cards.add(card);
        }

        deckRepository.save(deck);

        get = cardService.getTopicCards(topic.getId());

        assertEquals(cards, get);
        assertEquals(get.size(), 10);
    }

    @Test
    public void addCardByCardDtoTest() {
//        cardRepository.deleteAll();

        Card card = Generator.generateCard();
        Deck deck = Generator.generateDeck();

        deck = deckRepository.save(deck);

        CardDto cardDto = new CardDto();

        cardDto.setAnswer(card.getAnswer());
        cardDto.setDeckid(deck.getId());
        cardDto.setQuestion(card.getQuestion());

        cardService.addCardByCardDto(cardDto);
        assertTrue(cardRepository.findAll().iterator().hasNext());
    }

}