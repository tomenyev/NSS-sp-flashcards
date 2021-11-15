package cz.cvut.ear.flashcards.service;

import cz.cvut.ear.flashcards.Generator;
import cz.cvut.ear.flashcards.exception.NotFoundException;
import cz.cvut.ear.flashcards.model.Deck;
import cz.cvut.ear.flashcards.model.Topic;
import cz.cvut.ear.flashcards.repository.DeckRepository;
import cz.cvut.ear.flashcards.repository.TopicRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class DeckServiceTest {
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

    @Test
    public void addDeckTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
        Deck deck = Generator.generateDeck();
        deck.setId(null);
        deck = deckService.addDeck(topic.getId(), deck);
        assertNotNull(deck);
    }

    @Test
    public void getDeckTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
        Deck deck = Generator.generateDeck();
        deck.setId(null);
        deck = deckService.addDeck(topic.getId(), deck);
        assertNotNull(deck);
        deck = deckService.getDeck(deck.getId());
        assertNotNull(deck);
    }

    @Test
    public void updateDeckTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
        Deck deck = Generator.generateDeck();
        deck.setId(null);
        deck = deckService.addDeck(topic.getId(), deck);
        assertNotNull(deck);

        String name = deck.getName();
        deck.setName("newdeckname");

        deckService.updateDeck(topic.getId(), deck.getId(), deck);

        deck = deckService.getDeck(deck.getId());

        assertNotSame(deck.getName(), name);
        assertEquals(deck.getName(), "newdeckname");
    }

    @Test(expected = NotFoundException.class)
    public void deleteDeckTest() {
        Topic topic = Generator.generateTopic();
        topic.setId(null);
        topic = topicService.addTopic(topic);
        assertNotNull(topic);
        Deck deck = Generator.generateDeck();
        deck.setId(null);
        deck = deckService.addDeck(topic.getId(), deck);
        assertNotNull(deck);

        deckService.deleteDeck(deck.getId());
        deckService.getDeck(deck.getId());
    }

//    @Test
//    public void getUserDecksTest() {
//        Role role = Role.USER;
//
//        assertNotNull(role);
//
//        User user = Generator.generateUser();
//        user.setRoles(Collections.singleton(role));
//        user.setId(null);
//
//        user = userServiceImpl.addUser(user);
//
//        assertNotNull(user);
//
//        topicRepository.deleteAll();
//
//        Topic topic = Generator.generateTopic();
//        topic.setAuthor(null);
//        userServiceImpl.addTopic(user.getId(), topic);
//        topic = topicService.getAllTopics().iterator().next();
//
//        for (int i = 0; i < 10; i++) {
//            Deck deck = Generator.generateDeck();
//            deck.setId(null);
//            deck.addTopic(topic);
//            deck = deckRepository.save(deck);
//            topic.addDeck(deck);
//            assertNotNull(deck);
//        }
//
//        topicRepository.save(topic);
//
//        Set<Deck> decks = deckRepository.findAll();
//        Set<Deck> check = deckService.getUserDecks(user.getId());
//        assertEquals(decks, check);
//        assertEquals(10, decks.size());
//    }

//    @Test
//    public void getAllTopicDecksTest() {
//
//        topicRepository.deleteAll();
//
//        Topic topic = Generator.generateTopic();
//        topic.setAuthor(null);
//        topic = topicService.addTopic(topic);
//
//        for (int i = 0; i < 10; i++) {
//            Deck deck = Generator.generateDeck();
//            deck.setId(null);
//            deck.addTopic(topic);
//            deck = deckRepository.save(deck);
//            topic.addDeck(deck);
//            assertNotNull(deck);
//        }
//
//        topicRepository.save(topic);
//
//        Set<Deck> decks = deckRepository.findAll();
//        Set<Deck> check = deckService.getAllTopicDecks(topic.getId());
//        assertEquals(decks, check);
//        assertEquals(10, decks.size());
//
//    }

//    @Test
//    public void addDeckByDeckDtoTest() {
//        Topic topic = Generator.generateTopic();
//        topic.setAuthor(null);
//        topic = topicService.addTopic(topic);
//
//        Deck deck = Generator.generateDeck();
//        DeckDto deckDto = new DeckDto();
//        deckDto.setDeckAuthor(deck.getAuthor());
//        deckDto.setDeckName(deck.getName());
//        deckDto.setTopicid(topic.getId().toString());
//
//        deckRepository.deleteAll();
//
//        deckService.addDeckByDeckDto(deckDto);
//
//        assertEquals(deckRepository.findAll().size(), 1);
//
//    }

}

